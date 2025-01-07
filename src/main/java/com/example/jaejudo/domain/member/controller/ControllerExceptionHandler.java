package com.example.jaejudo.domain.member.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseVO> handleBindException(BindException e) {
        return ResponseEntity.badRequest().body(new ResponseVO(
                "BE",
                "Bind Exception",
                e.getFieldErrors())
        );
    }

    @Getter
    @AllArgsConstructor
    public static class ResponseVO {
        private String code;
        private String message;
        private List<FieldError> errors;
    }
}
