package com.example.jaejudo.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode{

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    VERIFICATION_FAILED(HttpStatus.CONFLICT, "인증에 실패하였습니다."),
    CANNOT_SEND_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, "이메일을 발송할 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
