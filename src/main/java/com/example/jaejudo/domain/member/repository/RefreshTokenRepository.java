package com.example.jaejudo.domain.member.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final ValueOperations<String, Object> valueOperations;
    private static final String PREFIX = "Refresh:";

    public void save(String userId, String refreshToken, long durationMillis) {
        valueOperations.set(PREFIX + userId, refreshToken, durationMillis, TimeUnit.MILLISECONDS);
    }

    public String findByUserId(String userId) {
        Object value = valueOperations.get(PREFIX + userId);
        return value != null ? value.toString() : null;
    }

    public void delete(String userId) {
        valueOperations.getOperations().delete(PREFIX + userId);
    }
}
