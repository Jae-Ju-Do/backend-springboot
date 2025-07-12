package com.example.jaejudo.global;

import com.example.jaejudo.domain.member.dto.request.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

@Slf4j
public class JsonLoginFilter
        extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    public JsonLoginFilter(ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher("/api/auth/login", "POST"));
        setSessionAuthenticationStrategy(new SessionFixationProtectionStrategy());
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            LoginRequest loginDTO = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            UsernamePasswordAuthenticationToken authRequest = getAuthRequest(loginDTO);

            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new AuthenticationServiceException("JSON 형식의 로그인만 가능합니다.", e);
        }
    }

    private static UsernamePasswordAuthenticationToken getAuthRequest(LoginRequest loginRequest) {

        String userId = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        if (userId == null || password == null) {
            throw new IllegalArgumentException("유효하지 않은 아이디 혹은 비밀번호입니다.");
        } else if (userId.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("아이디 혹은 비밀번호는 공백일 수 없습니다.");
        }

        log.info("Auth request: {}, {}", userId, password);
        return new UsernamePasswordAuthenticationToken(userId, password);
    }
}
