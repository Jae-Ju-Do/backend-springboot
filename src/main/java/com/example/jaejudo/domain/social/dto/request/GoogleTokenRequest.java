package com.example.jaejudo.domain.social.dto.request;

import lombok.Builder;

public record GoogleTokenRequest(
        String code,
        String client_id,
        String client_secret,
        String redirect_uri,
        String grant_type
) {

    @Builder
    public GoogleTokenRequest {}
}
