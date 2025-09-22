package com.example.jaejudo.domain.apikey.dto.response;

import java.time.LocalDateTime;

public record ApiKeyListResponse(String name, LocalDateTime createdAt,
                                 LocalDateTime expiresAt, boolean active) {}
