package com.example.jaejudo.global.exception.handler;

import com.example.jaejudo.domain.member.dto.response.ErrorResponse;
import com.example.jaejudo.global.exception.UserIdAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.example.jaejudo.domain.member")
@Slf4j
public class MemberExceptionHandler extends AbstractExceptionHandler {

    @ExceptionHandler(UserIdAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserIdAlreadyExistsException(UserIdAlreadyExistsException e) {
        return handleErrorCode(e.getErrorCode());
    }
}
