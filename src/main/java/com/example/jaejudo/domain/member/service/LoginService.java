package com.example.jaejudo.domain.member.service;

import com.example.jaejudo.domain.member.dto.UserDetailsAdapter;
import com.example.jaejudo.domain.member.entity.Member;
import com.example.jaejudo.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByUserId(username);
        if (member == null) {
            throw new UsernameNotFoundException(username);
        }

        return new UserDetailsAdapter(member);
    }
}
