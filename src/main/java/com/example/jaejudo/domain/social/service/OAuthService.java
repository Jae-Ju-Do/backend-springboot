package com.example.jaejudo.domain.social.service;

import com.example.jaejudo.domain.social.dto.response.OAuthResponse;

public interface OAuthService {
    OAuthResponse verify(String code);
}
