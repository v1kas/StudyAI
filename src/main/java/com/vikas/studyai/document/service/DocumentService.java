package com.vikas.studyai.document.service;

import com.vikas.studyai.document.dto.DocumentResponse;
import com.vikas.studyai.document.dto.DocumentStatusResponse;
import com.vikas.studyai.document.dto.DocumentUploadResponse;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;

public interface DocumentService {
    DocumentUploadResponse upload(MultipartFile file);
    DocumentResponse getById(UUID id);
    List<DocumentResponse> getAll();
    DocumentStatusResponse getStatus(UUID id);
    void delete(UUID id);
}
