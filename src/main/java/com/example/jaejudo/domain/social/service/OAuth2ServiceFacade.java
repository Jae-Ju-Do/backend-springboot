package com.example.jaejudo.domain.social.service;

import com.example.jaejudo.domain.member.entity.Member;
import com.example.jaejudo.domain.member.repository.MemberRepository;
import com.example.jaejudo.domain.social.dto.OAuth2MemberInfo;
import com.example.jaejudo.global.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OAuth2ServiceFacade {

    private final List<OAuth2Service> oAuth2Services;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    public String getAuthorizeUrl(String provider) {
        return findService(provider).getAuthorizeUrl();
    }

    public String loginOrJoin(String provider, String code) {

        OAuth2Service service = findService(provider);
        OAuth2MemberInfo memberInfo = service.getMemberInfo(code);
        Member member = memberRepository.findByProviderAndProviderId(
                memberInfo.provider(), memberInfo.providerId()
        ).orElseGet(() -> join(memberInfo));
        return jwtTokenProvider.generateToken(member.getUserId(), List.of("USER"));
    }

    private OAuth2Service findService(String provider) {

        return oAuth2Services.stream()
                .filter(s -> s.getProviderName().equals(provider))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private Member join(OAuth2MemberInfo memberInfo) {
        Member member = Member.builder()
                .name(memberInfo.name())
                .email(memberInfo.email())
                .userId(memberInfo.provider() + "_" + memberInfo.providerId())
                .password(passwordEncoder.encode(memberInfo.provider() + "_" + generatePassword()))
                .provider(memberInfo.provider())
                .providerId(memberInfo.providerId())
                .build();
        return memberRepository.save(member);
    }

    private String generatePassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            password.append(random.nextInt(10));
        }
        return password.toString();
    }
}
