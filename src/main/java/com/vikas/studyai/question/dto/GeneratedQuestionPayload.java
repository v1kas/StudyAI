package com.vikas.studyai.question.dto;

import com.vikas.studyai.question.entity.Difficulty;
import com.vikas.studyai.question.entity.QuestionType;
import java.util.List;
import java.util.UUID;

public record GeneratedQuestionPayload(String question, QuestionType type, Difficulty difficulty,
                                       String expectedAnswer, List<UUID> sourceChunkIds) { }
