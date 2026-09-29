package com.vikas.studyai.rag.service;

import com.vikas.studyai.rag.model.DocumentChunk;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class PostgresDocumentChunkStore implements DocumentChunkStore {
    private final JdbcTemplate jdbcTemplate;

    public PostgresDocumentChunkStore(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void replaceForDocument(UUID documentId, List<DocumentChunk> chunks, List<List<Double>> embeddings) {
        if (chunks.size() != embeddings.size()) {
            throw new IllegalArgumentException("Each document chunk must have one embedding");
        }
        jdbcTemplate.update("DELETE FROM document_chunks WHERE document_id = ?", documentId);
        for (int index = 0; index < chunks.size(); index++) {
            DocumentChunk chunk = chunks.get(index);
            jdbcTemplate.update("""
                    INSERT INTO document_chunks (document_id, chunk_index, page_number, content, embedding, metadata)
                    VALUES (?, ?, ?, ?, CAST(? AS vector), CAST(? AS jsonb))
                    """, chunk.documentId(), chunk.chunkIndex(), chunk.pageNumber(), chunk.content(),
                    asPgVector(embeddings.get(index)), "{\"pageNumber\":" + chunk.pageNumber() + "}");
        }
    }

    private String asPgVector(List<Double> embedding) {
        return "[" + embedding.stream().map(String::valueOf).collect(java.util.stream.Collectors.joining(",")) + "]";
    }
}
