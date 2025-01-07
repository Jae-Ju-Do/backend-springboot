package com.example.jaejudo.domain.member.controller;

import com.example.jaejudo.domain.member.dto.JoinRequestDTO;
import com.example.jaejudo.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @RequestMapping(value = "/members/new", method = RequestMethod.POST)
    public void newMember(@RequestBody JoinRequestDTO member) {

        // todo: Exception 처리 및 JSON Response 구현하기
        memberService.join(member);
    }
}
