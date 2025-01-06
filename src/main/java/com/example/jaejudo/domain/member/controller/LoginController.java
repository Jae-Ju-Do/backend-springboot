package com.example.jaejudo.domain.member.controller;

import com.example.jaejudo.domain.member.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    public void login() {

    }
}
