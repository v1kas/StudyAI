package com.vikas.studyai.study.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "study_sessions")
public class StudySession {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @Column(nullable = false) private UUID documentId;
    @Column(nullable = false, updatable = false) private Instant startedAt = Instant.now();
    private Instant completedAt;

    protected StudySession() { }
    public StudySession(UUID documentId) { this.documentId = documentId; }
    public UUID getId() { return id; }
    public UUID getDocumentId() { return documentId; }
    public Instant getStartedAt() { return startedAt; }
    public Instant getCompletedAt() { return completedAt; }
    public void complete() { this.completedAt = Instant.now(); }
}
