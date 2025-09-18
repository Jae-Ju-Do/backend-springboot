package com.example.jaejudo.domain.member.service;

import com.example.jaejudo.domain.member.dto.request.JoinRequest;
import com.example.jaejudo.domain.member.entity.Member;
import com.example.jaejudo.domain.member.repository.MemberRepository;
import com.example.jaejudo.global.exception.EmailAlreadyExistsException;
import com.example.jaejudo.global.exception.UserIdAlreadyExistsException;
import com.example.jaejudo.global.exception.errorcode.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void join(JoinRequest joinRequest) throws UserIdAlreadyExistsException {

        if (memberRepository.existsByEmail(joinRequest.getEmail())) {
            throw new EmailAlreadyExistsException(MemberErrorCode.EMAIL_ALREADY_EXISTS);
        }

        try {
            memberRepository.save(createMember(joinRequest));
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("DB 중복 키 에러 발생");
        }
    }

    private Member createMember(JoinRequest joinRequest) {

        return Member.builder()
                .name(joinRequest.getName())
                .email(joinRequest.getEmail())
                .password(passwordEncoder.encode(joinRequest.getPassword()))
                .provider(null)
                .providerId(null)
                .build();
    }
}
