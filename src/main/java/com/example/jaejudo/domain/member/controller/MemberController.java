package com.example.jaejudo.domain.member.controller;

import com.example.jaejudo.domain.member.dto.request.JoinRequest;
import com.example.jaejudo.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @RequestMapping(value = "/members/new", method = RequestMethod.POST)
    public void newMember(@Valid @RequestBody JoinRequest member,
                          BindingResult result) throws BindException {

        // todo: 이메일 인증, 아이디 중복검사 버튼 분리
        // todo: 여유 되면 전화번호 인증까지
        if (result.hasErrors()) {
            throw new BindException(result);
        }
        memberService.join(member);
    }
}
