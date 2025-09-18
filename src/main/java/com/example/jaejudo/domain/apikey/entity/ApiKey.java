package com.example.jaejudo.domain.apikey.entity;

import com.example.jaejudo.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String apiKey;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean active;

    @Builder
    public ApiKey(String apiKey, String name, String description, Member member, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.apiKey = apiKey;
        this.name = name;
        this.description = description;
        this.member = member;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public boolean isValid() {
        return active && expiresAt.isAfter(LocalDateTime.now());
    }
}
