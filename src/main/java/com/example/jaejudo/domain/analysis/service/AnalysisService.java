package com.example.jaejudo.domain.analysis.service;

import com.example.jaejudo.domain.analysis.enums.JobStatus;
import com.example.jaejudo.domain.member.entity.Member;
import com.example.jaejudo.domain.member.repository.MemberRepository;
import com.example.jaejudo.global.provider.JwtTokenProvider;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.example.jaejudo.domain.analysis.dto.AnalysisDto.*;
import com.example.jaejudo.domain.analysis.entity.Job;
import com.example.jaejudo.domain.analysis.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final S3Service s3Service;
    private final JobRepository jobRepository;
    private final MemberRepository memberRepository; // 서비스 계층에서만 리포지토리 접근
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate restTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String COOKIE_VALUE = "appproxy_permit=\"NGJiYjY1YjBmZTdhMDkyNmUyOGE0NmQ2Njg4ZWNiMjU4ZmNmYmY0MDNhMzAyOTJjYjM0OWExMjk2YTVkYzE0Zg==\"";
    private final long STATUS_TTL_SECONDS = 60 * 60;

    @Value("${fastapi.url}")
    private String fastApiUrl;

    /**
     * 1. 분석 요청 시작
     */
    @Transactional
    public JobResponse initiateAnalysis(MultipartFile file, String accessToken) {
        Member member = resolveMemberFromToken(accessToken); // 내부 헬퍼 메서드 사용
        String jobId = UUID.randomUUID().toString();
        String s3Key;

        try {
            s3Key = s3Service.upload(file, "exe/" + jobId);
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }

        Job job = Job.builder()
                .jobId(jobId)
                .member(member) // 비회원이면 null
                .createdAt(LocalDateTime.now())
                .s3ExeKey(s3Key)
                .status(JobStatus.PROCESSING)
                .build();
        jobRepository.save(job);

        // [Redis] Redis에 상세 상태 'QUEUED' 저장 (1시간)
        String statusKey = getJobStatusKey(jobId);
        redisTemplate.opsForValue().set(
                statusKey,
                JobStatus.QUEUED.name(), // "QUEUED"
                STATUS_TTL_SECONDS,
                TimeUnit.SECONDS
        );

        triggerFastApi(new AnalysisRequestMessage(jobId, s3Key));
        return new JobResponse(jobId, JobStatus.PROCESSING, "분석이 시작되었습니다.");
    }

    @Async
    public void triggerFastApi(AnalysisRequestMessage message) {
        try {
            String requestUrl = fastApiUrl + "/api/v1/workflow/start";

            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Cookie", COOKIE_VALUE); // 쿠키 추가

            // 요청 엔티티 생성 (Body + Header)
            HttpEntity<AnalysisRequestMessage> requestEntity = new HttpEntity<>(message, headers);
            restTemplate.postForEntity(requestUrl, requestEntity, Void.class);
            log.info("FastAPI trigger success for jobId: {}", message.getJobId());
        } catch (Exception e) {
            log.error("FastAPI trigger failed for jobId: {}", message.getJobId(), e);
        }
    }

    /**
     * 2. 작업 상태 상세 조회
     */
    @Transactional(readOnly = true)
    public JobDetailResponse getJobStatus(String jobId) {
        Job job = jobRepository.findByJobId(jobId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Job ID입니다."));

        String statusKey = getJobStatusKey(jobId);
        String statusString = (String) redisTemplate.opsForValue().get(statusKey);

        JobStatus status;
        if (statusString != null)
            status = JobStatus.valueOf(statusString);
        else
            status = job.getStatus();

        return JobDetailResponse.builder()
                .jobId(job.getJobId())
                .status(status)
                .createdAt(job.getCreatedAt())
                .completedAt(job.getCompletedAt())
                .downloadUrl(job.getStatus() == JobStatus.COMPLETED ?
                        s3Service.generatePresignedUrl(job.getS3PdfKey()) : null)
                .message(status.getDescription())
                .errorMessage(job.getErrorMessage())
                .build();
    }

    /**
     * 3. FastAPI 중간 콜백 (Redis만 업데이트)
     */
    public void updateJobProgress(AnalysisProgressRequest request) {
        String jobId = request.getJobId();
        JobStatus status = request.getStatus();
        String statusKey = getJobStatusKey(request.getJobId());

        // [Redis] 상세 상태만 업데이트 (TTL 갱신)
        redisTemplate.opsForValue().set(
                statusKey,
                status.name(), // 예: "DECOMPILING"
                STATUS_TTL_SECONDS,
                TimeUnit.SECONDS
        );
        log.info("Job Progress Update [Redis]: {} -> {}", jobId, status);
    }

    /**
     * 4. FastAPI 최종 콜백 처리
     */
    @Transactional
    public void handleAnalysisCompletion(AnalysisCompleteRequest request) {
        Job job = jobRepository.findByJobId(request.getJobId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Job ID입니다."));

        if (request.isSuccess()) {
            job.complete(request.getS3PdfKey());
            log.info("분석 완료: {}", job.getJobId());
        } else {
            job.fail(request.getErrorMessage());
            log.error("분석 실패: {}, 에러: {}", job.getJobId(), request.getErrorMessage());
        }
        redisTemplate.delete(getJobStatusKey(request.getJobId()));
    }

    /**
     * 5. 특정 회원의 분석 내역 조회
     */
    @Transactional(readOnly = true)
    public List<JobDetailResponse> getMemberHistory(String accessToken) {
        Member member = resolveMemberFromToken(accessToken);

        // 회원 전용 기능이므로 멤버가 없으면 예외 또는 null 반환
        if (member == null) {
            return null; // 또는 throw new UnAuthorizedException();
        }

        return jobRepository.findAllByMemberOrderByCreatedAtDesc(member).stream()
                .map(job -> JobDetailResponse.builder()
                        .jobId(job.getJobId())
                        .status(job.getStatus())
                        .createdAt(job.getCreatedAt())
                        .completedAt(job.getCompletedAt())
                        .downloadUrl(job.getStatus() == JobStatus.COMPLETED ?
                                s3Service.generatePresignedUrl(job.getS3PdfKey()) : null)
                        .message(job.getStatus().getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    // 토큰에서 Member 엔티티를 찾는 내부 헬퍼 메서드
    private Member resolveMemberFromToken(String accessToken) {
        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            String email = jwtTokenProvider.getEmailFromToken(accessToken);
            return memberRepository.findByEmail(email);
        }
        return null;
    }

    // Redis 키를 만드는 헬퍼 메서드
    private String getJobStatusKey(String jobId) {
        return "job:status:" + jobId;
    }
}