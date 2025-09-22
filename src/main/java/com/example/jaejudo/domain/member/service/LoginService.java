package com.example.jaejudo.domain.member.service;

import com.example.jaejudo.domain.member.dto.UserDetailsAdapter;
import com.example.jaejudo.domain.member.entity.Member;
import com.example.jaejudo.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(username);
        if (member == null) {
            log.error("존재하지 않는 이메일: {}", username);
            throw new UsernameNotFoundException(username);
        }

        log.info("User found");
        return new UserDetailsAdapter(member);
    }
}
