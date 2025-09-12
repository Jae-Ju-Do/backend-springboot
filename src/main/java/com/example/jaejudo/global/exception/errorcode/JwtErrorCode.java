package com.example.jaejudo.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum JwtErrorCode implements ErrorCode {

    INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "잘못된 JWT 서명입니다."),
    MALFORMED_TOKEN(HttpStatus.BAD_REQUEST, "올바르지 않은 JWT 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "지원하지 않는 JWT 토큰입니다."),
    ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, "JWT 클레임이 비어있습니다.");

    private final HttpStatus status;
    private final String message;
}
