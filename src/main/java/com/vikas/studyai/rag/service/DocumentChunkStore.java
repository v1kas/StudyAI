package com.vikas.studyai.rag.service;

import com.vikas.studyai.rag.model.DocumentChunk;
import java.util.List;
import java.util.UUID;

public interface DocumentChunkStore {
    void replaceForDocument(UUID documentId, List<DocumentChunk> chunks, List<List<Double>> embeddings);
}
