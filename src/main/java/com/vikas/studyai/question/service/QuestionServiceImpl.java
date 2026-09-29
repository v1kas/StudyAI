package com.vikas.studyai.question.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vikas.studyai.ai.prompt.QuestionGenerationPrompt;
import com.vikas.studyai.ai.service.AiService;
import com.vikas.studyai.common.exception.ResourceNotFoundException;
import com.vikas.studyai.document.repository.DocumentRepository;
import com.vikas.studyai.question.dto.*;
import com.vikas.studyai.question.entity.Difficulty;
import com.vikas.studyai.question.entity.Question;
import com.vikas.studyai.question.entity.QuestionType;
import com.vikas.studyai.question.repository.QuestionRepository;
import com.vikas.studyai.rag.model.DocumentChunk;
import com.vikas.studyai.rag.retrieval.RetrievalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final DocumentRepository documentRepository;
    private final QuestionRepository questionRepository;
    private final RetrievalService retrievalService;
    private final AiService aiService;
    private final ObjectMapper objectMapper;

    public QuestionServiceImpl(DocumentRepository documentRepository, QuestionRepository questionRepository,
                               RetrievalService retrievalService, AiService aiService, ObjectMapper objectMapper) {
        this.documentRepository = documentRepository;
        this.questionRepository = questionRepository;
        this.retrievalService = retrievalService;
        this.aiService = aiService;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public QuestionGenerationResponse generate(UUID documentId, QuestionGenerationRequest request) {
        if (!documentRepository.existsById(documentId)) {
            throw new ResourceNotFoundException("Document", documentId);
        }
        List<DocumentChunk> chunks = retrievalService.retrieve(documentId,
                "key concepts, definitions, explanations, comparisons, and applications", 50);
        if (chunks.isEmpty()) {
            throw new IllegalStateException("The document has no indexed content yet");
        }
        String prompt = QuestionGenerationPrompt.build(context(chunks), request.numberOfQuestions(),
                request.difficulty().name(), List.of(QuestionType.values()));
        GeneratedQuestionsPayload payload = parse(aiService.generate(prompt));
        validate(payload, request, chunks);

        List<QuestionResponse> questions = payload.questions().stream().map(item -> {
            Question question = new Question(documentId, item.question(), item.type(), item.difficulty(), item.expectedAnswer());
            question.setSourceChunks(toJson(item.sourceChunkIds()));
            Question saved = questionRepository.save(question);
            return toResponse(saved);
        }).toList();
        return new QuestionGenerationResponse(questions);
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionResponse getById(UUID id) {
        return toResponse(questionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Question", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponse> findByDocumentId(UUID documentId) {
        return questionRepository.findByDocumentId(documentId).stream().map(this::toResponse).toList();
    }

    private GeneratedQuestionsPayload parse(String response) {
        try {
            return objectMapper.readValue(extractJson(response), GeneratedQuestionsPayload.class);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("The AI returned an invalid question payload", exception);
        }
    }

    private void validate(GeneratedQuestionsPayload payload, QuestionGenerationRequest request, List<DocumentChunk> chunks) {
        if (payload.questions() == null || payload.questions().size() != request.numberOfQuestions()) {
            throw new IllegalStateException("The AI did not return the requested number of questions");
        }
        Set<UUID> validChunkIds = chunks.stream().map(DocumentChunk::id).collect(java.util.stream.Collectors.toSet());
        for (GeneratedQuestionPayload question : payload.questions()) {
            if (question.question() == null || question.question().isBlank() || question.type() == null ||
                    question.difficulty() == null || question.expectedAnswer() == null || question.expectedAnswer().isBlank() ||
                    question.sourceChunkIds() == null || question.sourceChunkIds().isEmpty() ||
                    !validChunkIds.containsAll(question.sourceChunkIds())) {
                throw new IllegalStateException("The AI returned an invalid question");
            }
            if (request.difficulty() != QuestionGenerationRequest.RequestedDifficulty.MIXED &&
                    question.difficulty() != Difficulty.valueOf(request.difficulty().name())) {
                throw new IllegalStateException("The AI returned a question with an unexpected difficulty");
            }
        }
    }

    private String context(List<DocumentChunk> chunks) {
        return chunks.stream().map(chunk -> "[chunkId=%s, page=%d] %s".formatted(chunk.id(), chunk.pageNumber(), chunk.content()))
                .collect(java.util.stream.Collectors.joining("\n\n"));
    }

    private String toJson(List<UUID> ids) {
        try {
            return objectMapper.writeValueAsString(ids);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Unable to serialize source chunks", exception);
        }
    }

    private QuestionResponse toResponse(Question question) {
        return new QuestionResponse(question.getId(), question.getDocumentId(), question.getQuestionText(),
                question.getType(), question.getDifficulty(), question.getExpectedAnswer(), question.getCreatedAt());
    }

    private String extractJson(String content) {
        String trimmed = content.trim();
        if (trimmed.startsWith("```")) {
            int firstNewline = trimmed.indexOf('\n');
            int closingFence = trimmed.lastIndexOf("```");
            return firstNewline > 0 && closingFence > firstNewline ? trimmed.substring(firstNewline + 1, closingFence).trim() : trimmed;
        }
        return trimmed;
    }
}
