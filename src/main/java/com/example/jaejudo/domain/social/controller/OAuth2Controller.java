package com.example.jaejudo.domain.social.controller;

import com.example.jaejudo.domain.social.service.OAuth2ServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/oauth2")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2ServiceFacade oAuth2ServiceFacade;

    @GetMapping("/{provider}")
    public ResponseEntity<?> redirectToLogin(@PathVariable String provider) {
        return ResponseEntity
                .ok(Map.of("url", oAuth2ServiceFacade.getAuthorizeUrl(provider)));
    }

    @GetMapping("/callback/{provider}")
    public ResponseEntity<?> handleCallback(
            @PathVariable String provider, @RequestParam String code) {
        Map<String, String> tokens = oAuth2ServiceFacade.loginOrJoin(provider, code);
        return ResponseEntity.ok(Map.of(
                "accessToken", tokens.get("accessToken"),
                "refreshToken", tokens.get("refreshToken"),
                "message", provider + " 로그인 성공"
        ));
    }
}
