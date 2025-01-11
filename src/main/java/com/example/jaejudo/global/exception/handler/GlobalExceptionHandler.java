package com.example.jaejudo.global.exception.handler;

import com.example.jaejudo.global.dto.ErrorResponse;
import com.example.jaejudo.global.exception.CannotSendMailException;
import com.example.jaejudo.global.exception.RedisNullException;
import com.example.jaejudo.global.exception.VerificationFailedException;
import com.example.jaejudo.global.exception.errorcode.CommonErrorCode;
import com.example.jaejudo.global.exception.errorcode.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends AbstractExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException e) {

        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        List<ErrorResponse.ValidationError> errors = e.getFieldErrors()
                .stream().map(ErrorResponse.ValidationError::of)
                .toList();

        return ResponseEntity.status(errorCode.getStatus()).body(
                ErrorResponse.builder()
                        .code(errorCode.name())
                        .message(errorCode.getMessage())
                        .errors(errors)
                        .build()
        );
    }

    @ExceptionHandler(CannotSendMailException.class)
    public ResponseEntity<ErrorResponse> handleCannotSendMailException(CannotSendMailException e) {
        return handleErrorCode(e.getErrorCode());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleErrorCode(errorCode);
    }

    @ExceptionHandler(VerificationFailedException.class)
    public ResponseEntity<ErrorResponse> handleVerificationFailedException(VerificationFailedException e) {
        return handleErrorCode(e.getErrorCode());
    }

    @ExceptionHandler(RedisNullException.class)
    public ResponseEntity<ErrorResponse> handleEmailTimeoutException(RedisNullException e) {
        return handleErrorCode(e.getErrorCode());
    }
}
