package com.example.jaejudo.domain.member.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailVerificationRequest {

    private String email;
    private String key;
}
