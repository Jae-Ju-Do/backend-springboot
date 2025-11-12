package com.example.jaejudo.global.config;

import com.example.jaejudo.domain.member.repository.BlacklistTokenRepository;
import com.example.jaejudo.domain.member.service.JwtTokenService;
import com.example.jaejudo.domain.member.service.LoginService;
import com.example.jaejudo.global.filter.JwtAuthenticationFilter;
import com.example.jaejudo.global.provider.CustomAuthenticationProvider;
import com.example.jaejudo.global.filter.JsonLoginFilter;
import com.example.jaejudo.global.provider.JwtTokenProvider;
import com.example.jaejudo.global.config.handler.LoginFailureHandler;
import com.example.jaejudo.global.config.handler.LoginSuccessHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginService loginService;
    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenService jwtTokenService;
    private final BlacklistTokenRepository blacklistTokenRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // CORS 설정 추가
        http.cors(Customizer.withDefaults());

        // CSRF 인증 해제
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement((session) ->
                session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        // JSON 로그인 설정
        http.formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(
                        jsonLoginFilter(http),
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, blacklistTokenRepository),
                        UsernamePasswordAuthenticationFilter.class);

        // OAuth2 로그인 설정


        // URL 권한 설정
        http.authorizeHttpRequests(request -> request
                .requestMatchers("/**").permitAll());

        return http.build();
    }

    @Bean
    public JsonLoginFilter jsonLoginFilter(HttpSecurity http) throws Exception {

        JsonLoginFilter filter = new JsonLoginFilter(objectMapper);
        filter.setAuthenticationManager(authenticationManager(http));
        filter.setAuthenticationSuccessHandler(loginSuccessHandler());
        filter.setAuthenticationFailureHandler(loginFailureHandler());
        filter.setSecurityContextRepository(
                new DelegatingSecurityContextRepository(
                        new RequestAttributeSecurityContextRepository(),
                        new HttpSessionSecurityContextRepository()
                )
        );
        return filter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(
                List.of("http://localhost:3000", "http://jaejudo.ai.kr", "https://jaejudo.ai.kr"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(customAuthenticationProvider())
                .build();
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(objectMapper, jwtTokenService);
    }

    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler(objectMapper);
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(loginService, passwordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
