package com.vikas.studyai.answer.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "answers")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @Column(nullable = false) private UUID questionId;
    @Column(name = "user_answer", nullable = false, columnDefinition = "text") private String userAnswer;
    @Column(nullable = false, updatable = false) private Instant submittedAt = Instant.now();
    public Answer(UUID questionId, String userAnswer) { this.questionId = questionId; this.userAnswer = userAnswer; }
}
