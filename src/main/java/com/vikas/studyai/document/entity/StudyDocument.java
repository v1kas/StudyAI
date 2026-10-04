package com.vikas.studyai.document.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyDocument {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String fileName;
    @Column(nullable = false)
    private String filePath;
    @Column(nullable = false)
    private long fileSize;
    private Integer pageCount;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentStatus status = DocumentStatus.UPLOADED;
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
    @Column(nullable = false)
    private Instant updatedAt = Instant.now();
    @Column(columnDefinition = "text")
    private String errorMessage;

    public StudyDocument(String fileName, String filePath, long fileSize) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }
    public void setStatus(DocumentStatus status) { this.status = status; this.updatedAt = Instant.now(); }
    public void setPageCount(Integer pageCount) { this.pageCount = pageCount; this.updatedAt = Instant.now(); }
    public void setProcessingError(String errorMessage) { this.errorMessage = errorMessage; this.updatedAt = Instant.now(); }
}
