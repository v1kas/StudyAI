package com.vikas.studyai.evaluation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record EvaluationResponse(UUID id, UUID answerId, BigDecimal overallScore,
                                 @JsonProperty("correctness") BigDecimal correctnessScore,
                                 @JsonProperty("completeness") BigDecimal completenessScore,
                                 @JsonProperty("relevance") BigDecimal relevanceScore,
                                 @JsonProperty("clarity") BigDecimal clarityScore,
                                 String feedback, String correctAnswer, List<String> missingConcepts,
                                 List<String> incorrectConcepts, Instant createdAt) { }
