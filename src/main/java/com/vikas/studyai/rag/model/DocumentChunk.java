package com.vikas.studyai.rag.model;

import java.util.UUID;

/** A searchable, citeable piece of one PDF page. */
public record DocumentChunk(UUID id, UUID documentId, int pageNumber, int chunkIndex, String content) { }
