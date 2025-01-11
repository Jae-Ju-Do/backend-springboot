package com.example.jaejudo.global.exception.handler;

import com.example.jaejudo.global.dto.ErrorResponse;
import com.example.jaejudo.global.exception.errorcode.ErrorCode;
import org.springframework.http.ResponseEntity;

// ControllerAdvice 의 후처리 로직 추출을 위한 추상클래스
// Exception 추가에 따라 추상화 분리, Strategy 패턴 적용 등 리팩토링 가능성 열어둠
public abstract class AbstractExceptionHandler {

    // handle 로직 분리
    ResponseEntity<ErrorResponse> handleErrorCode(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getStatus()).body(
                createErrorResponse(errorCode.name(), errorCode.getMessage())
        );
    }

    // utility methods
    private ErrorResponse createErrorResponse(String code, String message) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .build();
    }
}
