package com.example.jaejudo.domain.analysis.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JobStatus {

    // Redis
    QUEUED("대기 중"),
    DOWNLOADING("파일 다운로드 중"),
    SHA256_CONVERSION("파일 무결성 검사 중"),
    VIRUS_CHECK("악성코드 분석 중"),
    DECOMPILING("Ghidra 디컴파일 중"),
    LLM_ANALYSIS("LLM 분석 중"),
    REPORT_GENERATION("보고서 생성 중"),
    UPLOADING("결과 업로드 중"),

    // DB
    PROCESSING("처리 중"), // DB 저장용 기본값
    COMPLETED("완료"),
    FAILED("실패");

    private final String description;
}
