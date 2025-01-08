package com.example.jaejudo.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@AllArgsConstructor
public class BindErrorResponse {

    private final List<FieldError> fiendErrors;
}
