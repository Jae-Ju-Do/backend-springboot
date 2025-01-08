package com.example.jaejudo.domain.member.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@AllArgsConstructor
public class BindErrorResponseVo {

    private final List<FieldError> fiendErrors;
}
