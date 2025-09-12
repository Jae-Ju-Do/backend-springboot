package com.example.jaejudo.domain.member.controller;

import com.example.jaejudo.domain.member.service.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LogoutController {

    private final LogoutService logoutService;

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.substring(7); // "Bearer " 제거
        logoutService.logout(accessToken);
        return ResponseEntity.ok(Map.of(
                "message", "로그아웃 성공."
        ));
    }
}
