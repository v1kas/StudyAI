package com.vikas.studyai.evaluation.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "evaluations")
public class Evaluation {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @Column(nullable = false, unique = true) private UUID answerId;
    @Column(name = "overall_score", nullable = false, precision = 5, scale = 2) private BigDecimal overallScore;
    @Column(name = "correctness_score", nullable = false, precision = 5, scale = 2) private BigDecimal correctnessScore;
    @Column(name = "completeness_score", nullable = false, precision = 5, scale = 2) private BigDecimal completenessScore;
    @Column(name = "relevance_score", nullable = false, precision = 5, scale = 2) private BigDecimal relevanceScore;
    @Column(name = "clarity_score", nullable = false, precision = 5, scale = 2) private BigDecimal clarityScore;
    @Column(nullable = false, columnDefinition = "text") private String feedback;
    @Column(nullable = false, columnDefinition = "text") private String correctAnswer;
    @Column(nullable = false, columnDefinition = "jsonb") private String missingConcepts = "[]";
    @Column(nullable = false, columnDefinition = "jsonb") private String incorrectConcepts = "[]";
    @Column(nullable = false, updatable = false) private Instant createdAt = Instant.now();
    protected Evaluation() { }
    public Evaluation(UUID answerId, BigDecimal overallScore, BigDecimal correctnessScore,
                      BigDecimal completenessScore, BigDecimal relevanceScore, BigDecimal clarityScore,
                      String feedback, String correctAnswer) {
        this.answerId = answerId; this.overallScore = overallScore; this.correctnessScore = correctnessScore;
        this.completenessScore = completenessScore; this.relevanceScore = relevanceScore;
        this.clarityScore = clarityScore; this.feedback = feedback; this.correctAnswer = correctAnswer;
    }
    public UUID getId() { return id; }
    public UUID getAnswerId() { return answerId; }
    public BigDecimal getOverallScore() { return overallScore; }
    public BigDecimal getCorrectnessScore() { return correctnessScore; }
    public BigDecimal getCompletenessScore() { return completenessScore; }
    public BigDecimal getRelevanceScore() { return relevanceScore; }
    public BigDecimal getClarityScore() { return clarityScore; }
    public String getFeedback() { return feedback; }
    public String getCorrectAnswer() { return correctAnswer; }
    public String getMissingConcepts() { return missingConcepts; }
    public String getIncorrectConcepts() { return incorrectConcepts; }
    public void setConcepts(String missingConcepts, String incorrectConcepts) {
        this.missingConcepts = missingConcepts;
        this.incorrectConcepts = incorrectConcepts;
    }
    public Instant getCreatedAt() { return createdAt; }
}
