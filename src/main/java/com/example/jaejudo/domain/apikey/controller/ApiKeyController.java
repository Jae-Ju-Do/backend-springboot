package com.example.jaejudo.domain.apikey.controller;

import com.example.jaejudo.domain.apikey.dto.request.DeleteKeyRequest;
import com.example.jaejudo.domain.apikey.dto.request.GenerateKeyRequest;
import com.example.jaejudo.domain.apikey.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @DeleteMapping("/delete")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteKey(
            @RequestHeader("Authorization") String authorization,
            @RequestBody DeleteKeyRequest request) {
        String accessToken = authorization.substring(7); // "Bearer " 제거
        apiKeyService.deleteKey(request, accessToken);
        return ResponseEntity.ok(Map.of("message", "삭제 성공"));
    }

    @GetMapping("/getKeys")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getKeys(
            @RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.substring(7);
        return ResponseEntity.ok(apiKeyService.getKeyList(accessToken));
    }
}
