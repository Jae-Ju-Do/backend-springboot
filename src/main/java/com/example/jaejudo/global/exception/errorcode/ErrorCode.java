package com.example.jaejudo.global.exception.errorcode;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    String name();
    HttpStatus getStatus();
    String getMessage();
}
