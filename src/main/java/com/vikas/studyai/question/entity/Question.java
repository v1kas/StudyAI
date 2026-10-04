package com.vikas.studyai.question.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID documentId;

    @Column(name = "question_text", nullable = false, columnDefinition = "text")
    private String questionText;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private QuestionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Column(nullable = false, columnDefinition = "text")
    private String expectedAnswer;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "source_chunks", nullable = false, columnDefinition = "jsonb")
    private String sourceChunks = "[]";

    @Column(nullable = false, updatable = false)
    private java.time.Instant createdAt = java.time.Instant.now();

    public Question(UUID documentId, String questionText, QuestionType type, Difficulty difficulty, String expectedAnswer) {
        this.documentId = documentId; this.questionText = questionText; this.type = type;
        this.difficulty = difficulty; this.expectedAnswer = expectedAnswer;
    }
    public void setSourceChunks(String sourceChunks) { this.sourceChunks = sourceChunks; }
}
