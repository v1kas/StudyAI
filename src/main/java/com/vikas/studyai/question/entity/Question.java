package com.vikas.studyai.question.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "questions")
public class Question {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @Column(nullable = false) private UUID documentId;
    @Column(name = "question_text", nullable = false, columnDefinition = "text") private String questionText;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private QuestionType type;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Difficulty difficulty;
    @Column(nullable = false, columnDefinition = "text") private String expectedAnswer;
    @Column(nullable = false, columnDefinition = "jsonb") private String sourceChunks = "[]";
    @Column(nullable = false, updatable = false) private java.time.Instant createdAt = java.time.Instant.now();
    protected Question() { }
    public Question(UUID documentId, String questionText, QuestionType type, Difficulty difficulty, String expectedAnswer) {
        this.documentId = documentId; this.questionText = questionText; this.type = type;
        this.difficulty = difficulty; this.expectedAnswer = expectedAnswer;
    }
    public UUID getId() { return id; }
    public UUID getDocumentId() { return documentId; }
    public String getQuestionText() { return questionText; }
    public QuestionType getType() { return type; }
    public Difficulty getDifficulty() { return difficulty; }
    public String getExpectedAnswer() { return expectedAnswer; }
    public String getSourceChunks() { return sourceChunks; }
    public void setSourceChunks(String sourceChunks) { this.sourceChunks = sourceChunks; }
    public java.time.Instant getCreatedAt() { return createdAt; }
}
