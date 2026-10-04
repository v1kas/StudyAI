package com.vikas.studyai.study.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "study_sessions")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudySession {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @Column(nullable = false) private UUID documentId;
    @Column(nullable = false, updatable = false) private Instant startedAt = Instant.now();
    private Instant completedAt;

    public StudySession(UUID documentId) { this.documentId = documentId; }
    public void complete() { this.completedAt = Instant.now(); }
}
