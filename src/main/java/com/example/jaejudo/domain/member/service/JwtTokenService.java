package com.example.jaejudo.domain.member.service;

import com.example.jaejudo.domain.member.repository.RefreshTokenRepository;
import com.example.jaejudo.global.exception.JwtAuthenticationException;
import com.example.jaejudo.global.exception.errorcode.JwtErrorCode;
import com.example.jaejudo.global.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public Map<String, String> getJwtTokens(String userId, List<String> roles) {
        String accessToken = jwtTokenProvider.generateAccessToken(userId, roles);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId, roles);

        refreshTokenRepository.save(userId, refreshToken, jwtTokenProvider.getRefreshTokenValidity());
        return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
    }

    public Map<String, String> reissueTokens(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            log.error("Invalid refresh token");
            throw new JwtAuthenticationException(JwtErrorCode.INVALID_SIGNATURE);
        }

        String userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        String storedToken = refreshTokenRepository.findByUserId(userId);
        if (storedToken == null || !storedToken.equals(refreshToken)) {
            log.error("Expired refresh token");
            throw new JwtAuthenticationException(JwtErrorCode.EXPIRED_TOKEN);
        }

        List<String> roles = jwtTokenProvider.getRoles(refreshToken);
        return getJwtTokens(userId, roles);
    }
}
