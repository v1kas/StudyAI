package com.vikas.studyai.question.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record GenerateQuestionsRequest(@Min(1) @Max(20) int count) { }
