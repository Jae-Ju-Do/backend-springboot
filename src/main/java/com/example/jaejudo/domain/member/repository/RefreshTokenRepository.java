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

    public void save(String email, String refreshToken, long durationMillis) {
        valueOperations.set(PREFIX + email, refreshToken, durationMillis, TimeUnit.MILLISECONDS);
    }

    public String findByEmail(String email) {
        Object value = valueOperations.get(PREFIX + email);
        return value != null ? value.toString() : null;
    }

    public void delete(String email) {
        valueOperations.getOperations().delete(PREFIX + email);
    }
}
