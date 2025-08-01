package com.example.jaejudo.domain.social.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.google")
public class GoogleOAuth2Properties {

    private String clientId;
    private String clientSecret;
    private List<String> scope;
    private String redirectUri;
    private String authorizationGrantType;
}
