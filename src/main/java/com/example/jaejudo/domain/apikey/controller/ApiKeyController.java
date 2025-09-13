package com.example.jaejudo.domain.apikey.controller;

import com.example.jaejudo.domain.apikey.dto.request.GenerateKeyRequest;
import com.example.jaejudo.domain.apikey.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/key")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @GetMapping("generate")
    public ResponseEntity<?> generateKey(@RequestBody GenerateKeyRequest request) {
        return ResponseEntity.ok(apiKeyService.generateKey(request));
    }
}
