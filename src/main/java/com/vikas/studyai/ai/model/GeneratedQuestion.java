package com.vikas.studyai.ai.model;

import com.vikas.studyai.question.entity.QuestionType;

public record GeneratedQuestion(String prompt, QuestionType type, String referenceAnswer) { }
