package com.example.jaejudo.global.config.handler;

import com.example.jaejudo.global.dto.ErrorResponse;
import com.example.jaejudo.global.exception.errorcode.MemberErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        log.error(exception.getMessage());

        MemberErrorCode errorCode = MemberErrorCode.INVALID_CREDENTIALS;
        if (exception instanceof UsernameNotFoundException) {
            errorCode = MemberErrorCode.USERNAME_NOT_FOUND;
        } else if (exception instanceof BadCredentialsException) {
            errorCode = MemberErrorCode.WRONG_PASSWORD;
        } else if (exception instanceof DisabledException) {
            errorCode = MemberErrorCode.ACCOUNT_DISABLED;
        } else if (exception instanceof LockedException) {
            errorCode = MemberErrorCode.ACCOUNT_LOCKED;
        } else if (exception instanceof InternalAuthenticationServiceException) {
            errorCode = MemberErrorCode.INTERNAL_ERROR;
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();

        response.setStatus(errorCode.getStatus().value());
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
