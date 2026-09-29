package com.vikas.studyai.rag.model;

import java.util.List;
import java.util.UUID;

public record RetrievedContext(UUID documentId, List<DocumentChunk> chunks) { }
