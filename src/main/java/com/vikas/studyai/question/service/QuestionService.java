package com.vikas.studyai.question.service;

import com.vikas.studyai.question.dto.QuestionResponse;
import java.util.List;
import java.util.UUID;
import com.vikas.studyai.question.dto.QuestionGenerationRequest;
import com.vikas.studyai.question.dto.QuestionGenerationResponse;

public interface QuestionService {
    QuestionGenerationResponse generate(UUID documentId, QuestionGenerationRequest request);
    QuestionResponse getById(UUID id);
    List<QuestionResponse> findByDocumentId(UUID documentId);
}
