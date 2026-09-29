package com.vikas.studyai.document.dto;

import com.vikas.studyai.document.entity.DocumentStatus;
import java.util.UUID;

public record DocumentUploadResponse(UUID documentId, String fileName, DocumentStatus status) { }
