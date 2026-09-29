package com.vikas.studyai.answer.dto;

import jakarta.validation.constraints.NotBlank;

public record SubmitAnswerRequest(@NotBlank String answer) { }
