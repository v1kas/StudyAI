package com.vikas.studyai.study.service;

import com.vikas.studyai.study.dto.CreateStudySessionRequest;
import com.vikas.studyai.study.dto.StudySessionResponse;
import com.vikas.studyai.study.dto.StudySummaryResponse;
import java.util.UUID;

public interface StudySessionService {
    StudySessionResponse create(CreateStudySessionRequest request);
    StudySessionResponse getById(UUID id);
    StudySummaryResponse getSummaryByDocumentId(UUID documentId);
}
