package com.example.jaejudo.domain.apikey.entity;

import com.example.jaejudo.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String key;

    private String name;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private boolean active;

    @Builder
    public ApiKey(String key, String name, String description, Member member, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.key = key;
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
