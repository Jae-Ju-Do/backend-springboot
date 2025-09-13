package com.example.jaejudo.domain.apikey.service;

import com.example.jaejudo.domain.apikey.dto.request.GenerateKeyRequest;
import com.example.jaejudo.domain.apikey.dto.response.ApiKeyResponse;
import com.example.jaejudo.domain.apikey.entity.ApiKey;
import com.example.jaejudo.domain.apikey.repository.ApiKeyRepository;
import com.example.jaejudo.domain.member.entity.Member;
import com.example.jaejudo.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final MemberRepository memberRepository;

    public ApiKeyResponse generateKey(GenerateKeyRequest request) {
        Member member = memberRepository.findByUserId(request.getUserId());
        ApiKey apiKey = ApiKey.builder()
                .key(generateSecureKey())
                .name(request.getName())
                .member(member)
                .description(request.getDescription())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(request.getValidDate()))
                .build();
        apiKeyRepository.save(apiKey);
        return new ApiKeyResponse(
                apiKey.getKey(), apiKey.getName(), apiKey.getDescription(),
                apiKey.getCreatedAt(), apiKey.getExpiresAt()
        );
    }

    private String generateSecureKey() {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
