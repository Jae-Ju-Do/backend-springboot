package com.example.jaejudo.domain.analysis.controller;

import com.example.jaejudo.domain.analysis.dto.AnalysisDto.AnalysisCompleteRequest;
import com.example.jaejudo.domain.analysis.dto.AnalysisDto.JobDetailResponse;
import com.example.jaejudo.domain.analysis.dto.AnalysisDto.JobResponse;
import com.example.jaejudo.domain.analysis.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;

    /**
     * EXE 파일 업로드 및 분석 요청 (회원/비회원 공통)
     */
    @PostMapping("/upload")
    public ResponseEntity<JobResponse> uploadExe(
            @RequestParam("file") MultipartFile file,
            @RequestHeader(value = "Authorization", required = false) String authorization) {

        String accessToken = extractToken(authorization);
        JobResponse response = analysisService.initiateAnalysis(file, accessToken);
        return ResponseEntity.accepted().body(response);
    }

    /**
     * 분석 상태 단건 조회 (Polling 용, 비회원도 가능)
     */
    @GetMapping("/status/{jobId}")
    public ResponseEntity<JobDetailResponse> getStatus(@PathVariable String jobId) {
        return ResponseEntity.ok(analysisService.getJobStatus(jobId));
    }

    /**
     * 내 분석 내역 조회 (회원 전용)
     */
    @GetMapping("/history")
    public ResponseEntity<List<JobDetailResponse>> getHistory(
            @RequestHeader("Authorization") String authorization) {

        String accessToken = extractToken(authorization);
        // 토큰이 없거나 유효하지 않으면 Service에서 예외 발생시킴 (또는 null 반환 후 여기서 401 처리)
        List<JobDetailResponse> history = analysisService.getMemberHistory(accessToken);
        if (history == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(history);
    }

    /**
     * FastAPI 웹훅
     */
    @PostMapping("/callback")
    public ResponseEntity<Void> analysisCallback(@RequestBody AnalysisCompleteRequest request) {
        analysisService.handleAnalysisCompletion(request);
        return ResponseEntity.ok().build();
    }

    // "Bearer " 접두어 제거 유틸 메서드
    private String extractToken(String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null; // 토큰 없음
    }
}