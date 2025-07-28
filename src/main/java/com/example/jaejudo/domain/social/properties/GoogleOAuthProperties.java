package com.example.jaejudo.domain.social.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.google")
public class GoogleOAuthProperties {

    private String clientId;
    private String clientSecret;
    private String scope;
    private String redirectUri;
    private String authorizationGrantType;
}
