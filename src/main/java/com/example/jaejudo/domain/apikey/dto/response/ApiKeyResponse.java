package com.example.jaejudo.domain.apikey.dto.response;

import java.time.LocalDateTime;

public record ApiKeyResponse(String key, String name, String description,
                             LocalDateTime createdAt, LocalDateTime expiresAt) {}
