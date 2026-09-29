package com.vikas.studyai.document.mapper;

import com.vikas.studyai.document.dto.DocumentResponse;
import com.vikas.studyai.document.entity.StudyDocument;

public final class DocumentMapper {
    private DocumentMapper() { }
    public static DocumentResponse toResponse(StudyDocument document) {
        return new DocumentResponse(document.getId(), document.getFileName(), document.getFilePath(),
                document.getFileSize(), document.getPageCount(), document.getStatus(),
                document.getCreatedAt(), document.getUpdatedAt());
    }
}
