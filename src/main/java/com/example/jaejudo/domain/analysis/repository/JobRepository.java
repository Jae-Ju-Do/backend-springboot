package com.example.jaejudo.domain.analysis.repository;

import com.example.jaejudo.domain.analysis.entity.Job;
import com.example.jaejudo.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<Job> findByJobId(String jobId);
    List<Job> findAllByMemberOrderByCreatedAtDesc(Member member);
}
