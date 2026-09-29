package com.vikas.studyai.answer.controller;

import com.vikas.studyai.answer.dto.AnswerResponse;
import com.vikas.studyai.answer.dto.SubmitAnswerRequest;
import com.vikas.studyai.answer.service.AnswerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class AnswerController {
    private final AnswerService answerService;
    public AnswerController(AnswerService answerService) { this.answerService = answerService; }

    @PostMapping("/api/v1/questions/{questionId}/answers")
    @ResponseStatus(HttpStatus.CREATED)
    public AnswerResponse submit(@PathVariable UUID questionId, @Valid @RequestBody SubmitAnswerRequest request) {
        return answerService.submit(questionId, request);
    }

    @GetMapping("/api/v1/answers/{id}")
    public AnswerResponse getById(@PathVariable UUID id) { return answerService.getById(id); }
}
