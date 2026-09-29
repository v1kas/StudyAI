package com.vikas.studyai.question.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record QuestionGenerationRequest(
        @Min(1) @Max(20) int numberOfQuestions,
        @NotNull RequestedDifficulty difficulty
) {
    public enum RequestedDifficulty { EASY, MEDIUM, HARD, MIXED }
}
