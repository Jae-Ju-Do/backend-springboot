package com.example.jaejudo.domain.member.controller;

import com.example.jaejudo.domain.member.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    public void login() {

    }
}
