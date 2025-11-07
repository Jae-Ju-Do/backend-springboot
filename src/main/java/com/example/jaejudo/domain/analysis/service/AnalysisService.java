package com.example.jaejudo.domain.analysis.service;

import com.example.jaejudo.domain.analysis.enums.JobStatus;
import com.example.jaejudo.domain.member.entity.Member;
import com.example.jaejudo.domain.member.repository.MemberRepository;
import com.example.jaejudo.global.provider.JwtTokenProvider;
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
import java.util.List;
import java.util.UUID;
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

    @Value("${fastapi.url}")
    private String fastApiUrl;

    /**
     * 1. 분석 요청 시작
     * 변경: Long memberId -> String accessToken
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
                .s3ExeKey(s3Key)
                .status(JobStatus.PROCESSING)
                .build();
        jobRepository.save(job);

        triggerFastApi(new AnalysisRequestMessage(jobId, s3Key));

        return new JobResponse(jobId, JobStatus.PROCESSING, "분석이 시작되었습니다.");
    }

    @Async
    public void triggerFastApi(AnalysisRequestMessage message) {
        try {
            String requestUrl = fastApiUrl + "/api/v1/workflow/start";
            restTemplate.postForLocation(requestUrl, message);
            log.info("FastAPI trigger success for jobId: {}", message.getJobId());
        } catch (Exception e) {
            log.error("FastAPI trigger failed for jobId: {}", message.getJobId(), e);
        }
    }

    /**
     * 2. 작업 상태 상세 조회 (기존과 동일)
     */
    @Transactional(readOnly = true)
    public JobDetailResponse getJobStatus(String jobId) {
        Job job = jobRepository.findByJobId(jobId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Job ID입니다."));

        return JobDetailResponse.builder()
                .jobId(job.getJobId())
                .status(job.getStatus())
                .createdAt(job.getCreatedAt())
                .completedAt(job.getCompletedAt())
                .downloadUrl(job.getStatus() == JobStatus.COMPLETED ?
                        s3Service.generatePresignedUrl(job.getS3PdfKey()) : null)
                .errorMessage(job.getErrorMessage())
                .build();
    }

    /**
     * 3. FastAPI 콜백 처리 (기존과 동일)
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
    }

    /**
     * 4. 특정 회원의 분석 내역 조회
     * 변경: Long memberId -> String accessToken
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
}