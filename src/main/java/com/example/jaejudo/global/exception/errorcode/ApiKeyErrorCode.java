package com.example.jaejudo.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApiKeyErrorCode implements ErrorCode{

    DUPLICATED_NAME(HttpStatus.CONFLICT, "이미 존재하는 API Key 이름입니다.");

    private final HttpStatus status;
    private final String message;
}
