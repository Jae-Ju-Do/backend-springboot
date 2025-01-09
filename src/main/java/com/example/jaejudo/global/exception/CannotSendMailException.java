package com.example.jaejudo.global.exception;

public class CannotSendMailException extends RuntimeException {

    public CannotSendMailException() {
        super("이메일을 발송할 수 없습니다.");
    }
}
