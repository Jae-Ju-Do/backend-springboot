package com.example.jaejudo.domain.member.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@AllArgsConstructor
public class ErrorResponseVO {

    private final String code;
    private final String message;
}
