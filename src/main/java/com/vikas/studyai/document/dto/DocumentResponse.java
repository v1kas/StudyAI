package com.vikas.studyai.document.dto;

import com.vikas.studyai.document.entity.DocumentStatus;
import java.time.Instant;
import java.util.UUID;

public record DocumentResponse(UUID id, String fileName, String filePath, long fileSize,
                               Integer pageCount, DocumentStatus status, Instant createdAt, Instant updatedAt) { }
