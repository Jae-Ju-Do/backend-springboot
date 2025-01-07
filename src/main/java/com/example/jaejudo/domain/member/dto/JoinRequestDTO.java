package com.example.jaejudo.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRequestDTO {

    private String name;
    private String number;
    private String email;
    private String userId;
    private String password;
}
