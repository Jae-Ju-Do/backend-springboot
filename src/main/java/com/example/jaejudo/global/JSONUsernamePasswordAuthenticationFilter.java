package com.example.jaejudo.global;

import com.example.jaejudo.domain.member.dto.LoginDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
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
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class JSONUsernamePasswordAuthenticationFilter
        extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    public JSONUsernamePasswordAuthenticationFilter() {
        super(new AntPathRequestMatcher("/members/loginProcessURL", "POST"));
        setSessionAuthenticationStrategy(new SessionFixationProtectionStrategy());
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

        Map<String, String> map = objectMapper.readValue(messageBody, Map.class);

        String userId = map.get("userId");
        String password = map.get("password");
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(userId, password);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
