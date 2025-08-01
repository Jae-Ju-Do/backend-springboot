package com.example.jaejudo.domain.member.controller;

import com.example.jaejudo.domain.member.dto.request.JoinRequest;
import com.example.jaejudo.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
}
