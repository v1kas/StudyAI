package com.vikas.studyai.document.service;

import com.vikas.studyai.common.exception.InvalidDocumentException;
import com.vikas.studyai.common.exception.ResourceNotFoundException;
import com.vikas.studyai.document.dto.DocumentResponse;
import com.vikas.studyai.document.dto.DocumentStatusResponse;
import com.vikas.studyai.document.dto.DocumentUploadResponse;
import com.vikas.studyai.document.entity.DocumentStatus;
import com.vikas.studyai.document.entity.StudyDocument;
import com.vikas.studyai.document.mapper.DocumentMapper;
import com.vikas.studyai.document.repository.DocumentRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentServiceImpl implements DocumentService {
    private static final Logger log = LoggerFactory.getLogger(DocumentServiceImpl.class);
    private static final int MAX_PAGE_COUNT = 10;
    private final DocumentRepository documentRepository;
    private final Path uploadDirectory;
    private final ApplicationEventPublisher eventPublisher;

    public DocumentServiceImpl(DocumentRepository documentRepository,
                               @Value("${study-ai.storage.upload-dir:uploads}") String uploadDirectory,
                               ApplicationEventPublisher eventPublisher) {
        this.documentRepository = documentRepository;
        this.uploadDirectory = Path.of(uploadDirectory).toAbsolutePath().normalize();
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public DocumentUploadResponse upload(MultipartFile file) {
        validateUpload(file);
        int pageCount = getPageCount(file);

        String originalFileName = Path.of(file.getOriginalFilename()).getFileName().toString();
        Path storedFile = saveFile(file, originalFileName);
        StudyDocument document = new StudyDocument(originalFileName, storedFile.toString(), file.getSize());
        document.setPageCount(pageCount);
        StudyDocument savedDocument = documentRepository.save(document);
        log.info("event=DOCUMENT_UPLOAD documentId={} status={}", savedDocument.getId(), savedDocument.getStatus());
        eventPublisher.publishEvent(new DocumentUploadedEvent(savedDocument.getId()));

        return new DocumentUploadResponse(savedDocument.getId(), savedDocument.getFileName(), savedDocument.getStatus());
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentResponse getById(UUID id) {
        return DocumentMapper.toResponse(findDocument(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentResponse> getAll() {
        return documentRepository.findAll().stream().map(DocumentMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentStatusResponse getStatus(UUID id) {
        StudyDocument document = findDocument(id);
        return new DocumentStatusResponse(document.getId(), document.getStatus(), document.getErrorMessage());
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        documentRepository.delete(findDocument(id));
    }

    private void validateUpload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidDocumentException("A PDF file is required");
        }
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase(java.util.Locale.ROOT).endsWith(".pdf")) {
            throw new InvalidDocumentException("Only PDF files are supported");
        }
        try (var input = file.getInputStream()) {
            byte[] signature = input.readNBytes(5);
            if (signature.length != 5 || !"%PDF-".equals(new String(signature, java.nio.charset.StandardCharsets.US_ASCII))) {
                throw new InvalidDocumentException("The uploaded file is not a valid PDF");
            }
        } catch (IOException exception) {
            throw new InvalidDocumentException("Unable to read the uploaded file", exception);
        }
    }

    private int getPageCount(MultipartFile file) {
        try (PDDocument pdf = PDDocument.load(file.getBytes())) {
            int pageCount = pdf.getNumberOfPages();
            if (pageCount > MAX_PAGE_COUNT) {
                throw new InvalidDocumentException("A PDF may contain at most %d pages".formatted(MAX_PAGE_COUNT));
            }
            return pageCount;
        } catch (InvalidDocumentException exception) {
            throw exception;
        } catch (IOException exception) {
            throw new InvalidDocumentException("The uploaded file is not a readable PDF", exception);
        }
    }

    private Path saveFile(MultipartFile file, String originalFileName) {
        try {
            Files.createDirectories(uploadDirectory);
            Path destination = uploadDirectory.resolve(UUID.randomUUID() + "-" + originalFileName).normalize();
            if (!destination.startsWith(uploadDirectory)) {
                throw new InvalidDocumentException("Invalid file name");
            }
            try (var input = file.getInputStream()) {
                Files.copy(input, destination, StandardCopyOption.REPLACE_EXISTING);
            }
            return destination;
        } catch (IOException exception) {
            throw new InvalidDocumentException("Unable to save the uploaded file", exception);
        }
    }

    private StudyDocument findDocument(UUID id) {
        return documentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Document", id));
    }
}
