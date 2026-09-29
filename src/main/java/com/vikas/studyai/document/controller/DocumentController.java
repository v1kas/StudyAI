package com.vikas.studyai.document.controller;

import com.vikas.studyai.document.dto.DocumentUploadResponse;
import com.vikas.studyai.document.service.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.vikas.studyai.document.dto.DocumentResponse;
import com.vikas.studyai.document.dto.DocumentStatusResponse;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public DocumentUploadResponse upload(@RequestPart("file") MultipartFile file) {
        return documentService.upload(file);
    }

    @GetMapping
    public List<DocumentResponse> getAll() { return documentService.getAll(); }

    @GetMapping("/{id}")
    public DocumentResponse getById(@PathVariable UUID id) { return documentService.getById(id); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) { documentService.delete(id); }

    @GetMapping("/{id}/status")
    public DocumentStatusResponse getStatus(@PathVariable UUID id) { return documentService.getStatus(id); }
}
