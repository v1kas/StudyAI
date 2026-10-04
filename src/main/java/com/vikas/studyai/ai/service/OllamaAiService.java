package com.vikas.studyai.ai.service;

import com.vikas.studyai.common.exception.AiServiceException;
import org.springframework.ai.chat.client.ChatClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OllamaAiService implements AiService {
    private static final Logger log = LoggerFactory.getLogger(OllamaAiService.class);
    private final ChatClient chatClient;
    private final AiRequestRateLimiter rateLimiter;

    public OllamaAiService(ChatClient.Builder chatClientBuilder, AiRequestRateLimiter rateLimiter) {
        this.chatClient = chatClientBuilder.build();
        this.rateLimiter = rateLimiter;
    }

    @Override
    public String generate(String prompt) {
        if (prompt == null || prompt.isBlank()) {
            throw new IllegalArgumentException("A prompt is required");
        }
        rateLimiter.acquire();
        long startedNanos = System.nanoTime();
        try {
            log.info("event=LLM_REQUEST promptLength={}", prompt.length());
            String response = chatClient.prompt().user(prompt).call().content();
            log.info("event=LLM_RESPONSE responseLength={} durationMs={}", response == null ? 0 : response.length(),
                    (System.nanoTime() - startedNanos) / 1_000_000);
            return response;
        } catch (RuntimeException exception) {
            log.error("event=LLM_FAILURE durationMs={}", (System.nanoTime() - startedNanos) / 1_000_000, exception);
            throw new AiServiceException("Unable to generate an AI response", exception);
        }
    }
}
