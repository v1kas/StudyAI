package com.vikas.studyai.rag.retrieval;

import com.vikas.studyai.rag.model.DocumentChunk;
import java.util.List;
import java.util.UUID;

public interface RetrievalService {
    List<DocumentChunk> retrieve(UUID documentId, String query, int topK);
}
