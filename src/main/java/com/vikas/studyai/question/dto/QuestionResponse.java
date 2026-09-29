package com.vikas.studyai.question.dto;

import com.vikas.studyai.question.entity.Difficulty;
import com.vikas.studyai.question.entity.QuestionType;
import java.time.Instant;
import java.util.UUID;

public record QuestionResponse(UUID id, UUID documentId, String questionText, QuestionType type,
                               Difficulty difficulty, String expectedAnswer, Instant createdAt) { }
