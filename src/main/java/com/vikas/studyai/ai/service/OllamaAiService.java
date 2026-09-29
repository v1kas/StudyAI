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

    public OllamaAiService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public String generate(String prompt) {
        if (prompt == null || prompt.isBlank()) {
            throw new IllegalArgumentException("A prompt is required");
        }
        try {
            log.info("event=LLM_REQUEST promptLength={}", prompt.length());
            String response = chatClient.prompt().user(prompt).call().content();
            log.info("event=LLM_RESPONSE responseLength={}", response == null ? 0 : response.length());
            return response;
        } catch (RuntimeException exception) {
            throw new AiServiceException("Unable to generate an AI response", exception);
        }
    }
}
