package com.example.jaejudo.domain.member.service;

import com.example.jaejudo.domain.member.dto.request.JoinRequest;
import com.example.jaejudo.domain.member.dto.response.MemberResponse;
import com.example.jaejudo.domain.member.entity.Member;
import com.example.jaejudo.domain.member.repository.MemberRepository;
import com.example.jaejudo.global.exception.EmailAlreadyExistsException;
import com.example.jaejudo.global.exception.UserIdAlreadyExistsException;
import com.example.jaejudo.global.exception.errorcode.MemberErrorCode;
import com.example.jaejudo.global.provider.JwtTokenProvider;
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
    private final JwtTokenProvider jwtTokenProvider;

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

    public MemberResponse getMemberInfo(String accessToken) {
        Member member = memberRepository
                .findByEmail(jwtTokenProvider.getEmailFromToken(accessToken));
        return getMemberResponse(member);
    }

    public void deleteMember(String accessToken) {
        Member member = memberRepository
                .findByEmail(jwtTokenProvider.getEmailFromToken(accessToken));
        memberRepository.delete(member);
    }

    private Member createMember(JoinRequest joinRequest) {
        return Member.builder()
                .name(joinRequest.getName())
                .email(joinRequest.getEmail())
                .password(passwordEncoder.encode(joinRequest.getPassword()))
                .provider(null)
                .providerId(null)
                .profileImageUrl(null)
                .build();
    }

    private MemberResponse getMemberResponse(Member member) {
        return new MemberResponse(
                member.getName(),
                member.getEmail(),
                member.getProvider()
        );
    }
}
