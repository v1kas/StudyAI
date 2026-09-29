package com.vikas.studyai.study.controller;

import com.vikas.studyai.study.dto.StudySummaryResponse;
import com.vikas.studyai.study.service.StudySessionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class DocumentStudyController {
    private final StudySessionService studySessionService;
    public DocumentStudyController(StudySessionService studySessionService) { this.studySessionService = studySessionService; }

    @GetMapping("/api/v1/documents/{documentId}/study-summary")
    public StudySummaryResponse getSummary(@PathVariable UUID documentId) {
        return studySessionService.getSummaryByDocumentId(documentId);
    }
}
