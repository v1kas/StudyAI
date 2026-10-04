package com.vikas.studyai.rag.embedding;

import com.vikas.studyai.ai.service.AiRequestRateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpringAiEmbeddingService implements EmbeddingService {
    private static final Logger log = LoggerFactory.getLogger(SpringAiEmbeddingService.class);
    private final EmbeddingModel embeddingModel;
    private final AiRequestRateLimiter rateLimiter;

    public SpringAiEmbeddingService(EmbeddingModel embeddingModel, AiRequestRateLimiter rateLimiter) {
        this.embeddingModel = embeddingModel;
        this.rateLimiter = rateLimiter;
    }

    @Override
    public List<Double> embed(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Text to embed is required");
        }
        rateLimiter.acquire();
        long startedNanos = System.nanoTime();
        log.info("event=EMBEDDING_REQUEST inputLength={}", text.length());
        try {
            float[] embedding = embeddingModel.embed(text);
            List<Double> result = new ArrayList<>(embedding.length);
            for (float value : embedding) {
                result.add((double) value);
            }

            log.info("event=EMBEDDING_RESPONSE dimensions={} durationMs={}", result.size(),
                    (System.nanoTime() - startedNanos) / 1_000_000);
            return result;
        } catch (RuntimeException exception) {
            log.error("event=EMBEDDING_FAILURE durationMs={}", (System.nanoTime() - startedNanos) / 1_000_000, exception);
            throw exception;
        }
    }
}
