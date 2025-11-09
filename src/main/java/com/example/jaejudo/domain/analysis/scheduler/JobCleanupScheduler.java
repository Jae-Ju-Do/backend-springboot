package com.example.jaejudo.domain.analysis.scheduler;

import com.example.jaejudo.domain.analysis.entity.Job;
import com.example.jaejudo.domain.analysis.repository.JobRepository;
import com.example.jaejudo.domain.analysis.service.S3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobCleanupScheduler {

    private final JobRepository jobRepository;
    private final S3Service s3Service;

    // 매일 새벽 3시에 실행
    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void cleanupOldJobs() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(7); // 7일 지난 데이터 삭제 기준
        log.info("Starting cleanup of jobs older than {}", threshold);

        List<Job> oldJobs = jobRepository.findByCreatedAtBefore(threshold);
        for (Job job : oldJobs) {
            // 1. S3 파일 삭제 (비용 절약)
            s3Service.deleteFile(job.getS3ExeKey());
            if (job.getS3PdfKey() != null) {
                s3Service.deleteFile(job.getS3PdfKey());
            }
            // 2. DB 레코드 삭제
            jobRepository.delete(job);
        }
        log.info("Cleanup finished. Deleted {} jobs.", oldJobs.size());
    }
}