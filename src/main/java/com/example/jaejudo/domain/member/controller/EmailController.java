package com.example.jaejudo.domain.member.controller;

import com.example.jaejudo.domain.member.dto.request.EmailVerificationRequest;
import com.example.jaejudo.domain.member.service.EmailService;
import com.example.jaejudo.global.exception.VerificationFailedException;
import com.example.jaejudo.global.exception.errorcode.CommonErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @ResponseBody
    @PostMapping(value = "/members/sendEmail")
    public Map<String, String> sendEmail(@RequestBody @Valid EmailVerificationRequest email) {

        emailService.sendEmail(email.getEmail());

        Map<String, String> map = new HashMap<>();
        map.put("message", "이메일 발송 성공");
        return map;
    }

    @ResponseBody
    @PostMapping(value = "/members/verifyEmail")
    public Map<String, String> verifyEmail(@RequestBody EmailVerificationRequest key) {

        boolean verified = emailService.verifyEmail(key.getEmail(), key.getKey());
        if(!verified)
            throw new VerificationFailedException(CommonErrorCode.VERIFICATION_FAILED);

        Map<String, String> map = new HashMap<>();
        map.put("message", "인증 성공.");
        return map;
    }
}
