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

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Builder
    public Member(String name, String number, String email, String userId, String password) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.userId = userId;
        this.password = password;
    }
}
