package com.example.jaejudo.domain.member.controller;

import com.example.jaejudo.domain.member.dto.request.EmailVerificationRequest;
import com.example.jaejudo.domain.member.service.EmailService;
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

        String key = emailService.sendEmail(email.getEmail());

        Map<String, String> map = new HashMap<>();
        map.put("code", "200");
        map.put("key", key);
        return map;
    }

    @ResponseBody
    @PostMapping(value = "/members/verifyEmail")
    public Map<String, String> verifyEmail(@RequestBody EmailVerificationRequest key) {

        boolean verified = emailService.verifyEmail(key.getEmail(), key.getKey());
        Map<String, String> map = new HashMap<>();

        if (verified) {
            map.put("code", "200");
            map.put("message", "인증 완료");
        } else {
            map.put("code", "400");
            map.put("message", "인증 실패");
        }
        return map;
    }
}
