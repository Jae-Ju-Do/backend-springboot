package com.example.jaejudo.domain.member.service;

import com.example.jaejudo.domain.member.dto.request.JoinDTO;
import com.example.jaejudo.domain.member.entity.Member;
import com.example.jaejudo.domain.member.repository.MemberRepository;
import com.example.jaejudo.global.exception.UserIdAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void join(JoinDTO joinDTO) throws UserIdAlreadyExistsException {

        String userId = joinDTO.getUserId();
        if (memberRepository.existsByUserId(userId)) {
            throw new UserIdAlreadyExistsException(userId);
        }

        memberRepository.save(createMember(joinDTO));
    }

    private Member createMember(JoinDTO joinDTO) {

        return Member.builder()
                .name(joinDTO.getName())
                .number(joinDTO.getNumber())
                .email(joinDTO.getEmail())
                .userId(joinDTO.getUserId())
                .password(passwordEncoder.encode(joinDTO.getPassword()))
                .build();
    }
}
