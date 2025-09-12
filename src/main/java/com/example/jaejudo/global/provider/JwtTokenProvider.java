package com.example.jaejudo.global.provider;

import com.example.jaejudo.global.exception.JwtAuthenticationException;
import com.example.jaejudo.global.exception.errorcode.JwtErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtTokenProvider {

    private final SecretKey key = Jwts.SIG.HS256.key().build();
    @Getter
    private final long accessTokenValidity = 1000L * 60 * 30; // 30분
    @Getter
    private final long refreshTokenValidity = 1000L * 60 * 60 * 24 * 14; // 14일

    public String generateAccessToken(String username, List<String> roles) {
        return buildToken(username, roles, accessTokenValidity);
    }

    public String generateRefreshToken(String username, List<String> roles) {
        return buildToken(username, roles, refreshTokenValidity);
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

    public String getUserIdFromToken(String token) {
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
