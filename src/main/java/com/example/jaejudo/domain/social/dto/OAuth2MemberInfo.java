package com.example.jaejudo.domain.social.dto;

import lombok.Builder;

public record OAuth2MemberInfo (
        String provider,
        String providerId,
        String email,
        String name,
        String picture
) {

    @Builder
    public OAuth2MemberInfo {}
}
