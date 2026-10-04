package com.vikas.studyai.evaluation.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "evaluations")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "answer_id", nullable = false, unique = true)
    private UUID answerId;

    @Column(name = "overall_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal overallScore;

    @Column(name = "correctness_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal correctnessScore;

    @Column(name = "completeness_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal completenessScore;

    @Column(name = "relevance_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal relevanceScore;

    @Column(name = "clarity_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal clarityScore;

    @Column(nullable = false, columnDefinition = "text")
    private String feedback;

    @Column(name = "correct_answer", nullable = false, columnDefinition = "text")
    private String correctAnswer;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "missing_concepts", nullable = false, columnDefinition = "jsonb")
    private List<String> missingConcepts = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "incorrect_concepts", nullable = false, columnDefinition = "jsonb")
    private List<String> incorrectConcepts = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public Evaluation(
            UUID answerId,
            BigDecimal overallScore,
            BigDecimal correctnessScore,
            BigDecimal completenessScore,
            BigDecimal relevanceScore,
            BigDecimal clarityScore,
            String feedback,
            String correctAnswer
    ) {
        this.answerId = answerId;
        this.overallScore = overallScore;
        this.correctnessScore = correctnessScore;
        this.completenessScore = completenessScore;
        this.relevanceScore = relevanceScore;
        this.clarityScore = clarityScore;
        this.feedback = feedback;
        this.correctAnswer = correctAnswer;
    }

    public void setConcepts(
            List<String> missingConcepts,
            List<String> incorrectConcepts
    ) {
        this.missingConcepts = missingConcepts != null
                ? missingConcepts
                : new ArrayList<>();

        this.incorrectConcepts = incorrectConcepts != null
                ? incorrectConcepts
                : new ArrayList<>();
    }

}
