package com.example.jaejudo.domain.apikey.service;

import com.example.jaejudo.domain.apikey.dto.request.DeleteKeyRequest;
import com.example.jaejudo.domain.apikey.dto.request.GenerateKeyRequest;
import com.example.jaejudo.domain.apikey.dto.response.ApiKeyListResponse;
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
import java.util.List;

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

        if (apiKeyRepository.existsByMemberAndName(member, request.getName())) {
            log.error("중복된 API Key 이름: {}", request.getName());
            throw new ApiKeyException(ApiKeyErrorCode.DUPLICATED_NAME);
        }
        ApiKey apiKey = ApiKey.builder()
                .apiKey(generateSecureKey())
                .name(request.getName())
                .member(member)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(request.getValidDate()))
                .build();
        apiKeyRepository.save(apiKey);
        return new ApiKeyResponse(
                apiKey.getApiKey(), apiKey.getName(),
                apiKey.getCreatedAt(), apiKey.getExpiresAt()
        );
    }

    public void deleteKey(DeleteKeyRequest request, String accessToken) {
        Member member = memberRepository
                .findByEmail(jwtTokenProvider.getEmailFromToken(accessToken));
        String name = request.getName();

        ApiKey apiKey = apiKeyRepository.findByMemberAndName(member, name)
                .orElse(null);
        if (apiKey == null) {
            log.error("API Key 찾을 수 없음: {}", name);
            throw new ApiKeyException(ApiKeyErrorCode.API_KEY_NOT_FOUND);
        }
        apiKeyRepository.delete(apiKey);
    }

    public List<ApiKeyListResponse> getKeyList(String accessToken) {
        Member member = memberRepository
                .findByEmail(jwtTokenProvider.getEmailFromToken(accessToken));
        return apiKeyRepository.findAllByMemberOrderByCreatedAtDesc(member)
                .stream().map(this::createListResponse).toList();
    }

    private String generateSecureKey() {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    private ApiKeyListResponse createListResponse(ApiKey apiKey) {
        return new ApiKeyListResponse(
                apiKey.getName(),
                apiKey.getCreatedAt(),
                apiKey.getExpiresAt(),
                apiKey.isValid()
        );
    }
}
