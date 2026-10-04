package com.vikas.studyai.evaluation.dto;

import java.util.List;

public record EvaluationResult(Integer correctnessScore, Integer completenessScore, Integer relevanceScore,
                               Integer clarityScore, List<String> missingConcepts, List<String> incorrectConcepts,
                               String correctAnswer, String feedback) { }
