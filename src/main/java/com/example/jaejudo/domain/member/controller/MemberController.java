package com.example.jaejudo.domain.member.controller;

import com.example.jaejudo.domain.member.dto.JoinRequestDTO;
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
    public void newMember(@Valid @RequestBody JoinRequestDTO member,
                          BindingResult result) throws BindException {

        if (result.hasErrors()) {
            throw new BindException(result);
        }
        memberService.join(member);
    }
}
