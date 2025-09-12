package com.example.jaejudo.domain.member.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class BlacklistTokenRepository {

    private final ValueOperations<String, Object> valueOperations;
    private static final String PREFIX = "Blacklist:";

    public void save(String accessToken, long durationMillis) {
        valueOperations.set(PREFIX + accessToken, "logout", durationMillis, TimeUnit.MILLISECONDS);
    }

    public boolean exists(String accessToken) {
        return valueOperations.get(PREFIX + accessToken) != null;
    }
}
