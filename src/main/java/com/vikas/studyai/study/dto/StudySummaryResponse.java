package com.vikas.studyai.study.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record StudySummaryResponse(UUID documentId, int totalQuestions, int answered,
                                   BigDecimal averageScore, List<String> strongTopics, List<String> weakTopics) { }
