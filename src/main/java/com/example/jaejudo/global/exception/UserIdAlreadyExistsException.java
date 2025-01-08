package com.example.jaejudo.global.exception;

public class UserIdAlreadyExistsException extends RuntimeException {

    public UserIdAlreadyExistsException(String userId) {
        super(userId + " 는 이미 존재하는 아이디 입니다.");
    }
}
