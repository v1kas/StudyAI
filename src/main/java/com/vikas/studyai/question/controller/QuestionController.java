package com.vikas.studyai.question.controller;

import com.vikas.studyai.question.dto.QuestionGenerationRequest;
import com.vikas.studyai.question.dto.QuestionGenerationResponse;
import com.vikas.studyai.question.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.List;
import com.vikas.studyai.question.dto.QuestionResponse;

@RestController
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) { this.questionService = questionService; }

    @PostMapping("/api/v1/documents/{documentId}/questions")
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionGenerationResponse generate(@PathVariable(value = "documentId") UUID documentId,
                                               @Valid @RequestBody QuestionGenerationRequest request) {
        return questionService.generate(documentId, request);
    }

    @GetMapping("/api/v1/documents/{documentId}/questions")
    public List<QuestionResponse> getByDocument(@PathVariable(value = "documentId") UUID documentId) { return questionService.findByDocumentId(documentId); }

    @GetMapping("/api/v1/questions/{id}")
    public QuestionResponse getById(@PathVariable(value = "id") UUID id) { return questionService.getById(id); }
}
