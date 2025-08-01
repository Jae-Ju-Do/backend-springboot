package com.example.jaejudo.global.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    // 회원가입
    USER_ID_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 아이디 입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "해당 이메일로 등록된 계정이 이미 존재합니다."),

    // 로그인
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 아이디입니다."),
    WRONG_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 혹은 비밀번호가 잘못되었습니다."),
    ACCOUNT_DISABLED(HttpStatus.FORBIDDEN, "비활성화된 계정입니다."),
    ACCOUNT_LOCKED(HttpStatus.LOCKED, "잠긴 계정입니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 인증 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;
}
