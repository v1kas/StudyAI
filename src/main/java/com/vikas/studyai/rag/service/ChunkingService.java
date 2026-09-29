package com.vikas.studyai.rag.service;

import com.vikas.studyai.document.model.PageContent;
import com.vikas.studyai.rag.model.DocumentChunk;
import java.util.List;
import java.util.UUID;

public interface ChunkingService {
    List<DocumentChunk> chunk(UUID documentId, List<PageContent> pages);
}
