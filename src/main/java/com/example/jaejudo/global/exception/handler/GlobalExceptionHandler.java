package com.example.jaejudo.global.exception.handler;

import com.example.jaejudo.domain.member.dto.response.ErrorResponse;
import com.example.jaejudo.global.exception.CannotSendMailException;
import com.example.jaejudo.global.exception.errorcode.CommonErrorCode;
import com.example.jaejudo.global.exception.errorcode.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
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
}
