package com.example.jaejudo.domain.member.service;

import com.example.jaejudo.domain.member.dto.JoinRequestDTO;
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

    public void join(JoinRequestDTO joinRequestDTO) throws UserIdAlreadyExistsException {

        String userId = joinRequestDTO.getUserId();
        if (memberRepository.existsByUserId(userId)) {
            throw new UserIdAlreadyExistsException(userId);
        }

        memberRepository.save(createMember(joinRequestDTO));
    }

    private Member createMember(JoinRequestDTO joinRequestDTO) {

        return Member.builder()
                .name(joinRequestDTO.getName())
                .number(joinRequestDTO.getNumber())
                .email(joinRequestDTO.getEmail())
                .userId(joinRequestDTO.getUserId())
                .password(passwordEncoder.encode(joinRequestDTO.getPassword()))
                .build();
    }
}
