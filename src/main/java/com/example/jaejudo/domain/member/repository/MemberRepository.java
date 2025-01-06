package com.example.jaejudo.domain.member.repository;

import com.example.jaejudo.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByUserId(String userId);
    Member findByUserId(String userId);
}
