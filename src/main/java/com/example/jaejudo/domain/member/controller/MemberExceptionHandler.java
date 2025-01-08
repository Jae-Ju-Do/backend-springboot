package com.example.jaejudo.domain.member.controller;

import com.example.jaejudo.domain.member.dto.response.BindErrorResponse;
import com.example.jaejudo.domain.member.dto.response.ErrorResponse;
import com.example.jaejudo.global.exception.UserIdAlreadyExistsException;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class MemberExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<BindErrorResponse> handleBindException(BindException e) {
        return ResponseEntity.status(400).body(
                new BindErrorResponse(e.getFieldErrors())
        );
    }

    @ExceptionHandler(UserIdAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserIdAlreadyExistsException(UserIdAlreadyExistsException e) {
        return ResponseEntity.status(409).body(
                new ErrorResponse("409", e.getMessage())
        );
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ErrorResponse> handleMessagingException(MessagingException e) {
        return ResponseEntity.status(500).body(
                new ErrorResponse("500", e.getMessage())
        );
    }
}
