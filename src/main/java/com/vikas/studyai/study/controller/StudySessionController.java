package com.vikas.studyai.study.controller;

import com.vikas.studyai.study.dto.CreateStudySessionRequest;
import com.vikas.studyai.study.dto.StudySessionResponse;
import com.vikas.studyai.study.service.StudySessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/study-sessions")
public class StudySessionController {
    private final StudySessionService studySessionService;
    public StudySessionController(StudySessionService studySessionService) { this.studySessionService = studySessionService; }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public StudySessionResponse create(@Valid @RequestBody CreateStudySessionRequest request) { return studySessionService.create(request); }
    @GetMapping("/{id}")
    public StudySessionResponse get(@PathVariable(value = "id") UUID id) { return studySessionService.getById(id); }
}
