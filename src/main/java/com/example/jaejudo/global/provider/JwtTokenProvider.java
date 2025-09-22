package com.example.jaejudo.global.provider;

import com.example.jaejudo.global.exception.JwtAuthenticationException;
import com.example.jaejudo.global.exception.errorcode.JwtErrorCode;
import com.example.jaejudo.global.properties.JwtProperties;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtTokenProvider {

    private final SecretKey key;
    @Getter
    private final long accessTokenValidity;
    @Getter
    private final long refreshTokenValidity;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret()
                .getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidity = jwtProperties.getAccessTokenValidity(); // 30분
        this.refreshTokenValidity = jwtProperties.getRefreshTokenValidity(); // 14일
    }

    public String generateAccessToken(String emmail, List<String> roles) {
        return buildToken(emmail, roles, accessTokenValidity);
    }

    public String generateRefreshToken(String email, List<String> roles) {
        return buildToken(email, roles, refreshTokenValidity);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (SecurityException e) {
            log.error(e.getMessage(), e);
            throw new JwtAuthenticationException(JwtErrorCode.INVALID_SIGNATURE);
        } catch (MalformedJwtException e) {
            log.error(e.getMessage(), e);
            throw new JwtAuthenticationException(JwtErrorCode.MALFORMED_TOKEN);
        } catch (ExpiredJwtException e) {
            log.error(e.getMessage(), e);
            throw new JwtAuthenticationException(JwtErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.error(e.getMessage(), e);
            throw new JwtAuthenticationException(JwtErrorCode.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            throw new JwtAuthenticationException(JwtErrorCode.ILLEGAL_ARGUMENT);
        }
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key).build()
                .parseSignedClaims(token).getPayload()
                .getSubject();
    }

    public List<String> getRoles(String token) {
        Object roles = Jwts.parser()
                .verifyWith(key).build()
                .parseSignedClaims(token)
                .getPayload()
                .get("roles");

        if (roles instanceof List<?> list) {
            return list.stream()
                    .map(Object::toString)
                    .toList();
        }
        return List.of();
    }

    public long getExpiration(String token) {
        Date expiration = Jwts.parser()
                .verifyWith(key).build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

        return expiration.getTime() - System.currentTimeMillis();
    }

    private String buildToken(String username, List<String> roles, long validity) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validity);

        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }
}
