package com.vikas.studyai.answer.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "answers")
public class Answer {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @Column(nullable = false) private UUID questionId;
    @Column(name = "user_answer", nullable = false, columnDefinition = "text") private String userAnswer;
    @Column(nullable = false, updatable = false) private Instant submittedAt = Instant.now();
    protected Answer() { }
    public Answer(UUID questionId, String userAnswer) { this.questionId = questionId; this.userAnswer = userAnswer; }
    public UUID getId() { return id; }
    public UUID getQuestionId() { return questionId; }
    public String getUserAnswer() { return userAnswer; }
    public Instant getSubmittedAt() { return submittedAt; }
}
