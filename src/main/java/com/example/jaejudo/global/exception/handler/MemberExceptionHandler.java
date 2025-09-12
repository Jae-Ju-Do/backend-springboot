package com.example.jaejudo.global.exception.handler;

import com.example.jaejudo.global.dto.ErrorResponse;
import com.example.jaejudo.global.exception.EmailAlreadyExistsException;
import com.example.jaejudo.global.exception.JwtAuthenticationException;
import com.example.jaejudo.global.exception.UserIdAlreadyExistsException;
import com.example.jaejudo.global.exception.errorcode.MemberErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.example.jaejudo.domain.member")
public class MemberExceptionHandler extends AbstractExceptionHandler {

    @ExceptionHandler(UserIdAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserIdAlreadyExistsException(UserIdAlreadyExistsException e) {
        return handleErrorCode(e.getErrorCode());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        return handleErrorCode(e.getErrorCode());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return handleErrorCode(MemberErrorCode.USERNAME_NOT_FOUND);
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleJwtAuthenticationException(JwtAuthenticationException e) {
        return handleErrorCode(e.getErrorCode());
    }
}
