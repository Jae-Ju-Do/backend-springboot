package com.example.jaejudo.domain.member.service;

import com.example.jaejudo.domain.member.dto.request.JoinRequest;
import com.example.jaejudo.domain.member.entity.Member;
import com.example.jaejudo.domain.member.repository.MemberRepository;
import com.example.jaejudo.global.exception.UserIdAlreadyExistsException;
import com.example.jaejudo.global.exception.errorcode.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void join(JoinRequest joinRequest) throws UserIdAlreadyExistsException {

        String userId = joinRequest.getUserId();
        if (memberRepository.existsByUserId(userId)) {
            throw new UserIdAlreadyExistsException(MemberErrorCode.USER_ID_ALREADY_EXISTS);
        }

        memberRepository.save(createMember(joinRequest));
    }

    private Member createMember(JoinRequest joinRequest) {

        return Member.builder()
                .name(joinRequest.getName())
                .number(joinRequest.getNumber())
                .email(joinRequest.getEmail())
                .userId(joinRequest.getUserId())
                .password(passwordEncoder.encode(joinRequest.getPassword()))
                .build();
    }
}
