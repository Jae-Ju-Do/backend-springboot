package com.example.jaejudo.domain.analysis.dto;

import com.example.jaejudo.domain.analysis.enums.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AnalysisDto {

    // 초기 응답
    @Getter @AllArgsConstructor
    public static class JobResponse {
        private String jobId;
        private JobStatus status;
        private String message;
    }

    // 상세 조회 응답
    @Getter @Builder
    public static class JobDetailResponse {
        private String jobId;
        private JobStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime completedAt;
        private String downloadUrl; // Presigned URL
        private String message;
        private String errorMessage;
    }

    @Getter @NoArgsConstructor
    public static class AnalysisProgressRequest {
        private String jobId;
        private JobStatus status;
    }

    // FastAPI 요청 메시지
    @Getter @AllArgsConstructor
    public static class AnalysisRequestMessage {
        private String jobId;
        private String s3FileKey;
    }

    // FastAPI 콜백 요청
    @Getter @NoArgsConstructor
    public static class AnalysisCompleteRequest {
        private String jobId;
        private String s3PdfKey;
        private boolean success;
        private String errorMessage;
    }
}