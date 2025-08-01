package com.example.jaejudo.domain.member.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column
    private String provider;    // 소셜 로그인 (google, kakao 등)

    @Column
    private String providerId;  // 소셜 로그인 고유 아이디

    @Builder
    public Member(String name, String email, String userId, String password, String provider, String providerId) {
        this.name = name;
        this.email = email;
        this.userId = userId;
        this.password = password;
        this.provider = provider;
        this.providerId = providerId;
    }

    public boolean isSocialMember() {
        return provider != null;
    }
}
