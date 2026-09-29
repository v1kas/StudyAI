package com.vikas.studyai.evaluation.dto;

import java.util.List;

public record EvaluationResult(int correctnessScore, int completenessScore, int relevanceScore,
                               int clarityScore, List<String> missingConcepts, List<String> incorrectConcepts,
                               String correctAnswer, String feedback) { }
