package com.example.jaejudo.domain.member.controller;

import com.example.jaejudo.domain.member.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/jwt")
@RequiredArgsConstructor
public class JwtTokenController {

    private final JwtTokenService jwtTokenService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        Map<String, String> tokens = jwtTokenService.reissueTokens(refreshToken);
        return ResponseEntity.ok(tokens);
    }
}
