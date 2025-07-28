package com.example.jaejudo.domain.social.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class OAuthResponse {

    private final String provider;
    private final String providerId;
    private final String email;
    private final String name;
}
