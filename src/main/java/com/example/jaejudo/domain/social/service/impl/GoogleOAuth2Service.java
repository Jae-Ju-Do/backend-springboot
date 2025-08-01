package com.example.jaejudo.domain.social.service.impl;

import com.example.jaejudo.domain.social.dto.OAuth2MemberInfo;
import com.example.jaejudo.domain.social.dto.request.GoogleTokenRequest;
import com.example.jaejudo.domain.social.dto.response.GoogleTokenResponse;
import com.example.jaejudo.domain.social.dto.response.GoogleUserInfoResponse;
import com.example.jaejudo.domain.social.properties.GoogleOAuth2Properties;
import com.example.jaejudo.domain.social.service.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class GoogleOAuth2Service implements OAuth2Service {

    private final RestTemplate restTemplate;
    private final GoogleOAuth2Properties properties;

    @Override
    public String getAuthorizeUrl() {

        return UriComponentsBuilder
                .fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("client_id", properties.getClientId())
                .queryParam("redirect_uri", properties.getRedirectUri())
                .queryParam("response_type", "code")
                .queryParam("scope", String.join(" ", properties.getScope()))
                .queryParam("access_type", "offline")
                .toUriString();
    }

    @Override
    public OAuth2MemberInfo getMemberInfo(String code) {

        // Access Token 요청
        GoogleTokenRequest tokenRequest = GoogleTokenRequest.builder()
                .code(code)
                .client_id(properties.getClientId())
                .client_secret(properties.getClientSecret())
                .redirect_uri(properties.getRedirectUri())
                .grant_type(properties.getAuthorizationGrantType())
                .build();
        ResponseEntity<GoogleTokenResponse> tokenResponse = restTemplate.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                new HttpEntity<>(tokenRequest, getHeaders()),
                GoogleTokenResponse.class
        );
        GoogleTokenResponse googleToken = tokenResponse.getBody();
        if (googleToken == null || googleToken.getAccessToken() == null) {
            throw new IllegalStateException("구글 토큰 요청 실패");
        }

        // 사용자 정보 요청
        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.setBearerAuth(googleToken.getAccessToken());
        ResponseEntity<GoogleUserInfoResponse> userInfoResponse = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v3/userinfo",
                HttpMethod.GET,
                new HttpEntity<>(userInfoHeaders),
                GoogleUserInfoResponse.class
        );

        GoogleUserInfoResponse userInfo = userInfoResponse.getBody();
        if (userInfo == null) {
            throw new IllegalStateException("구글 사용자 정보 요청 실패");
        }

        // 소셜 로그인 공통 인터페이스로 변환
        return OAuth2MemberInfo.builder()
                .provider("google")
                .providerId(userInfo.getId())
                .name(userInfo.getName())
                .email(userInfo.getEmail())
                .build();
    }

    @Override
    public String getProviderName() {
        return "google";
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
