package com.example.jaejudo.domain.apikey.service;

import com.example.jaejudo.domain.apikey.dto.request.GenerateKeyRequest;
import com.example.jaejudo.domain.apikey.dto.response.ApiKeyResponse;
import com.example.jaejudo.domain.apikey.entity.ApiKey;
import com.example.jaejudo.domain.apikey.repository.ApiKeyRepository;
import com.example.jaejudo.domain.member.entity.Member;
import com.example.jaejudo.domain.member.repository.MemberRepository;
import com.example.jaejudo.global.exception.ApiKeyException;
import com.example.jaejudo.global.exception.errorcode.ApiKeyErrorCode;
import com.example.jaejudo.global.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private static final Logger log = LoggerFactory.getLogger(ApiKeyService.class);
    private final JwtTokenProvider jwtTokenProvider;
    private final ApiKeyRepository apiKeyRepository;
    private final MemberRepository memberRepository;

    public ApiKeyResponse generateKey(GenerateKeyRequest request, String accessToken) {
        Member member = memberRepository
                .findByEmail(jwtTokenProvider.getEmailFromToken(accessToken));

        if (apiKeyRepository.existsByName(request.getName())) {
            log.error("중복된 API Key 이름: {}", request.getName());
            throw new ApiKeyException(ApiKeyErrorCode.DUPLICATED_NAME);
        }
        ApiKey apiKey = ApiKey.builder()
                .apiKey(generateSecureKey())
                .name(request.getName())
                .member(member)
                .description(request.getDescription())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(request.getValidDate()))
                .build();
        apiKeyRepository.save(apiKey);
        return new ApiKeyResponse(
                apiKey.getApiKey(), apiKey.getName(), apiKey.getDescription(),
                apiKey.getCreatedAt(), apiKey.getExpiresAt()
        );
    }

    private String generateSecureKey() {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
