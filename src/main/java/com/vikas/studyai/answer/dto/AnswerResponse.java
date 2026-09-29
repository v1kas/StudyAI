package com.vikas.studyai.answer.dto;

import java.time.Instant;
import java.util.UUID;

public record AnswerResponse(UUID id, UUID questionId, String userAnswer, Instant submittedAt) { }
