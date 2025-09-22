package com.example.jaejudo.domain.apikey.repository;

import com.example.jaejudo.domain.apikey.entity.ApiKey;
import com.example.jaejudo.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    Optional<ApiKey> findByMemberAndName(Member member, String name);
    boolean existsByMemberAndName(Member member, String name);
    List<ApiKey> findAllByMemberOrderByCreatedAtDesc(Member member);
}
