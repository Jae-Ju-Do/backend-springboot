package com.example.jaejudo.domain.apikey.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public record ApiKeyResponse(String key, String name, String description,
                             LocalDateTime createdAt, LocalDateTime expiresAt) {}
