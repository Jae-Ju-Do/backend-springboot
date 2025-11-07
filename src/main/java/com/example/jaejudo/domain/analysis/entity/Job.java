package com.example.jaejudo.domain.analysis.entity;

import com.example.jaejudo.domain.analysis.enums.JobStatus;
import com.example.jaejudo.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 36)
    private String jobId; // 외부 노출용 UUID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // nullable = true
    private Member member; // 비회원은 null, 회원은 Member 엔티티

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    @Column(nullable = false)
    private String s3ExeKey; // 업로드된 EXE 파일 S3 경로

    @Column
    private String s3PdfKey; // 결과 PDF 파일 S3 경로

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime completedAt;

    public void complete(String s3PdfKey) {
        this.status = JobStatus.COMPLETED;
        this.s3PdfKey = s3PdfKey;
        this.completedAt = LocalDateTime.now();
    }

    public void fail(String errorMessage) {
        this.status = JobStatus.FAILED;
        this.errorMessage = errorMessage;
        this.completedAt = LocalDateTime.now();
    }

    public void assignMember(Member member) {
        this.member = member;
    }
}