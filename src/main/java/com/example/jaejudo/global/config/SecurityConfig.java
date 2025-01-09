package com.example.jaejudo.global.config;

import com.example.jaejudo.domain.member.service.LoginService;
import com.example.jaejudo.global.JSONLoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginService loginService;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // CSRF 인증 해제
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement((session) ->
                session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        // URL 권한 설정
        http.authorizeHttpRequests(request->request
                .requestMatchers("/**").permitAll());

        // JSON 로그인 설정
        http.formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(
                        getJSONLoginFilter(),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JSONLoginFilter getJSONLoginFilter() throws Exception {

        JSONLoginFilter filter = new JSONLoginFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setSecurityContextRepository(
                new DelegatingSecurityContextRepository(
                        new RequestAttributeSecurityContextRepository(),
                        new HttpSessionSecurityContextRepository()
                )
        );
        return filter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
