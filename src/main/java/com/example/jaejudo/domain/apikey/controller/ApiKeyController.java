package com.example.jaejudo.domain.apikey.controller;

import com.example.jaejudo.domain.apikey.dto.request.GenerateKeyRequest;
import com.example.jaejudo.domain.apikey.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/key")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @PostMapping("/generate")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> generateKey(
            @RequestHeader("Authorization") String authorization,
            @RequestBody GenerateKeyRequest request) {
        String accessToken = authorization.substring(7); // "Bearer " 제거
        return ResponseEntity.ok(apiKeyService.generateKey(request, accessToken));
    }
}
