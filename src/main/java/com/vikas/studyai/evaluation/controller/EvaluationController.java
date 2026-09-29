package com.vikas.studyai.evaluation.controller;

import com.vikas.studyai.evaluation.dto.EvaluationResponse;
import com.vikas.studyai.evaluation.service.EvaluationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class EvaluationController {
    private final EvaluationService evaluationService;
    public EvaluationController(EvaluationService evaluationService) { this.evaluationService = evaluationService; }

    @GetMapping("/api/v1/answers/{answerId}/evaluation")
    public EvaluationResponse getByAnswer(@PathVariable UUID answerId) {
        return evaluationService.getByAnswerId(answerId);
    }
}
