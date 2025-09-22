package com.example.jaejudo.domain.member.service;

import com.example.jaejudo.domain.member.repository.BlacklistTokenRepository;
import com.example.jaejudo.domain.member.repository.RefreshTokenRepository;
import com.example.jaejudo.global.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlacklistTokenRepository blacklistTokenRepository;

    public void logout(String accessToken) {
        String email = jwtTokenProvider.getEmailFromToken(accessToken);
        refreshTokenRepository.delete(email);

        long expiration = jwtTokenProvider.getExpiration(accessToken);
        blacklistTokenRepository.save(accessToken, expiration);
    }
}
