package com.example.jaejudo.domain.analysis.repository;

import com.example.jaejudo.domain.analysis.entity.Job;
import com.example.jaejudo.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<Job> findByJobId(String jobId);
    List<Job> findAllByMemberOrderByCreatedAtDesc(Member member);
    // (유지보수용) 특정 날짜 이전의 오래된 Job들 찾기 (삭제 배치 등에 사용)
    List<Job> findByCreatedAtBefore(LocalDateTime dateTime);
}
