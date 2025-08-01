package com.example.jaejudo.domain.member.repository;

import com.example.jaejudo.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByUserId(String userId);
    boolean existsByEmail(String email);
    Member findByUserId(String userId);
    Optional<Member> findByProviderAndProviderId(String provider, String providerId);
}
