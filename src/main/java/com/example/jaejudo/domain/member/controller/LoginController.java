package com.example.jaejudo.domain.member.controller;

import com.example.jaejudo.domain.member.dto.request.LoginRequest;
import com.example.jaejudo.domain.member.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping(value = "members/login")
    public void login(@RequestBody LoginRequest loginRequest) {

    }
}
