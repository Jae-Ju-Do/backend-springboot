package com.example.jaejudo.domain.member.controller;

import com.example.jaejudo.domain.member.dto.request.JoinRequest;
import com.example.jaejudo.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @ResponseBody
    @PostMapping(value = "/signup")
    public Map<String, String> newMember(@Valid @RequestBody JoinRequest member,
                         BindingResult result) throws BindException {

        if (result.hasErrors()) {
            throw new BindException(result);
        }
        memberService.join(member);

        Map<String, String> map = new HashMap<>();
        map.put("message", "회원가입 성공");
        return map;
    }

    @GetMapping("/info")
    public ResponseEntity<?> getMemberInfo(
            @RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.substring(7);
        return ResponseEntity.ok(memberService.getMemberInfo(accessToken));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMember(
            @RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.substring(7);
        memberService.deleteMember(accessToken);
        return ResponseEntity.ok(Map.of("message", "회원탈퇴 성공."));
    }
}
