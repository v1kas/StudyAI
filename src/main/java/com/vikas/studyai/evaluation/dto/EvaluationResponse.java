package com.vikas.studyai.evaluation.dto;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record EvaluationResponse(UUID id, UUID answerId, BigDecimal overallScore,
                                 BigDecimal correctnessScore,
                                 BigDecimal completenessScore,
                                 BigDecimal relevanceScore,
                                 BigDecimal clarityScore,
                                 String feedback, String correctAnswer, List<String> missingConcepts,
                                 List<String> incorrectConcepts, Instant createdAt) { }
