package com.vikas.studyai.study.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record StudySessionResponse(UUID id, UUID documentId, int totalQuestions, int answered,
                                   BigDecimal averageScore, List<String> strongTopics, List<String> weakTopics,
                                   Instant startedAt, Instant completedAt) { }
