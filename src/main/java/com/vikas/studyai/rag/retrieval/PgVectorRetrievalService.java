package com.vikas.studyai.rag.retrieval;

import com.vikas.studyai.rag.embedding.EmbeddingService;
import com.vikas.studyai.rag.model.DocumentChunk;
import com.vikas.studyai.common.exception.VectorSearchException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@Service
public class PgVectorRetrievalService implements RetrievalService {
    private static final Logger log = LoggerFactory.getLogger(PgVectorRetrievalService.class);
    private final EmbeddingService embeddingService;
    private final JdbcTemplate jdbcTemplate;

    public PgVectorRetrievalService(EmbeddingService embeddingService, JdbcTemplate jdbcTemplate) {
        this.embeddingService = embeddingService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<DocumentChunk> retrieve(UUID documentId, String query, int topK) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("A search query is required");
        }
        if (topK < 1 || topK > 50) {
            throw new IllegalArgumentException("topK must be between 1 and 50");
        }
        try {
            log.info("event=VECTOR_SEARCH documentId={} topK={}", documentId, topK);
            String queryVector = asPgVector(embeddingService.embed(query));
            return jdbcTemplate.query("""
                        SELECT id, document_id, page_number, chunk_index, content
                        FROM document_chunks
                        WHERE document_id = ?
                        ORDER BY embedding <=> CAST(? AS vector)
                        LIMIT ?
                        """,
                (resultSet, rowNumber) -> new DocumentChunk(
                        resultSet.getObject("id", UUID.class),
                        resultSet.getObject("document_id", UUID.class),
                        resultSet.getInt("page_number"),
                        resultSet.getInt("chunk_index"),
                        resultSet.getString("content")),
                    documentId, queryVector, topK);
        } catch (RuntimeException exception) {
            throw new VectorSearchException("Unable to retrieve document chunks", exception);
        }
    }

    private String asPgVector(List<Double> embedding) {
        return "[" + embedding.stream().map(String::valueOf).collect(java.util.stream.Collectors.joining(",")) + "]";
    }
}
