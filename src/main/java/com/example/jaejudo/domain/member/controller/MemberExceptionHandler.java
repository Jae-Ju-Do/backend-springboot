package com.example.jaejudo.domain.member.controller;

import com.example.jaejudo.domain.member.vo.BindErrorResponseVo;
import com.example.jaejudo.domain.member.vo.ErrorResponseVO;
import com.example.jaejudo.global.exception.UserIdAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class MemberExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<BindErrorResponseVo> handleBindException(BindException e) {
        return ResponseEntity.status(400).body(
                new BindErrorResponseVo(e.getFieldErrors())
        );
    }

    @ExceptionHandler(UserIdAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseVO> handleUserIdAlreadyExistsException(UserIdAlreadyExistsException e) {
        return ResponseEntity.status(409).body(
                new ErrorResponseVO("409", e.getMessage())
        );
    }
}
