package com.example.jaejudo.global.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    USER_ID_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 아이디 입니다.");

    private final HttpStatus status;
    private final String message;
}
