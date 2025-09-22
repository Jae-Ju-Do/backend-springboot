package com.example.jaejudo.domain.member.controller;

import com.example.jaejudo.domain.member.service.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LogoutController {

    private final LogoutService logoutService;

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.substring(7); // "Bearer " 제거
        logoutService.logout(accessToken);
        return ResponseEntity.ok(Map.of(
                "message", "로그아웃 성공."
        ));
    }
}
