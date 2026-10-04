package com.vikas.studyai.evaluation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vikas.studyai.ai.prompt.AnswerEvaluationPrompt;
import com.vikas.studyai.ai.service.AiService;
import com.vikas.studyai.answer.entity.Answer;
import com.vikas.studyai.answer.repository.AnswerRepository;
import com.vikas.studyai.common.exception.ResourceNotFoundException;
import com.vikas.studyai.evaluation.dto.EvaluationResponse;
import com.vikas.studyai.evaluation.dto.EvaluationResult;
import com.vikas.studyai.evaluation.entity.Evaluation;
import com.vikas.studyai.evaluation.repository.EvaluationRepository;
import com.vikas.studyai.question.entity.Question;
import com.vikas.studyai.question.repository.QuestionRepository;
import com.vikas.studyai.rag.model.DocumentChunk;
import com.vikas.studyai.rag.retrieval.RetrievalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class EvaluationServiceImpl implements EvaluationService {
    private static final Logger log = LoggerFactory.getLogger(EvaluationServiceImpl.class);
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final EvaluationRepository evaluationRepository;
    private final RetrievalService retrievalService;
    private final AiService aiService;
    private final ObjectMapper objectMapper;
    private final ScoringService scoringService;

    public EvaluationServiceImpl(AnswerRepository answerRepository, QuestionRepository questionRepository,
                                 EvaluationRepository evaluationRepository, RetrievalService retrievalService,
                                 AiService aiService, ObjectMapper objectMapper, ScoringService scoringService) {
        this.answerRepository = answerRepository; this.questionRepository = questionRepository;
        this.evaluationRepository = evaluationRepository; this.retrievalService = retrievalService;
        this.aiService = aiService; this.objectMapper = objectMapper; this.scoringService = scoringService;
    }

    @Override
    @Transactional
    public EvaluationResponse evaluate(UUID answerId) {
        log.info("event=ANSWER_EVALUATION answerId={}", answerId);

        // 1. Get Answer
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Answer", answerId));

        // 2. Get Question
        Question question = questionRepository.findById(answer.getQuestionId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Question", answer.getQuestionId()));

        log.info("event=EVALUATION_REFERENCE answerId={} expectedAnswerLength={}", answerId,
                question.getExpectedAnswer() == null ? 0 : question.getExpectedAnswer().length());

        // 3. Retrieve relevant document chunks using RAG
        List<DocumentChunk> chunks = retrievalService.retrieve(
                question.getDocumentId(),
                question.getQuestionText(),
                5
        );

        // 4. Build evaluation prompt and call LLM
        String referenceContext = context(chunks);
        log.info("event=EVALUATION_CONTEXT answerId={} retrievedChunks={} contextLength={}",
                answerId, chunks.size(), referenceContext.length());

        String prompt = AnswerEvaluationPrompt.build(
                question.getQuestionText(),
                question.getExpectedAnswer(),
                referenceContext,
                answer.getUserAnswer()
        );

        EvaluationResult result = parse(
                aiService.generate(prompt)
        );
        // Wrapper score types preserve missing fields as null, instead of silently turning them into zero.
        validateScores(result);
        log.info("event=EVALUATION_SCORES answerId={} correctness={} completeness={} relevance={} clarity={}",
                answerId, result.correctnessScore(), result.completenessScore(),
                result.relevanceScore(), result.clarityScore());

        // 5. Validate LLM scores
        // 6. Calculate final score in backend
        BigDecimal overallScore =
                scoringService.calculateOverallScore(result);

        // Create or refresh the evaluation so users can rerun a poor model result.
        Evaluation evaluation = evaluationRepository.findByAnswerId(answerId).orElseGet(() -> new Evaluation(
                answerId,
                overallScore,
                score(result.correctnessScore()),
                score(result.completenessScore()),
                score(result.relevanceScore()),
                score(result.clarityScore()),
                result.feedback(),
                result.correctAnswer()
        ));
        evaluation.setOverallScore(overallScore);
        evaluation.setCorrectnessScore(score(result.correctnessScore()));
        evaluation.setCompletenessScore(score(result.completenessScore()));
        evaluation.setRelevanceScore(score(result.relevanceScore()));
        evaluation.setClarityScore(score(result.clarityScore()));
        evaluation.setFeedback(result.feedback());
        evaluation.setCorrectAnswer(result.correctAnswer());

        // 8. Store JSONB fields as List<String>
        evaluation.setConcepts(
                result.missingConcepts(),
                result.incorrectConcepts()
        );

        // 9. Save evaluation
        Evaluation savedEvaluation =
                evaluationRepository.save(evaluation);
        log.info("event=EVALUATION_SAVED answerId={} evaluationId={} overallScore={} correctness={} completeness={} relevance={} clarity={}",
                answerId, savedEvaluation.getId(), savedEvaluation.getOverallScore(), savedEvaluation.getCorrectnessScore(),
                savedEvaluation.getCompletenessScore(), savedEvaluation.getRelevanceScore(), savedEvaluation.getClarityScore());

        // 10. Return response
        return toResponse(
                savedEvaluation,
                result.missingConcepts(),
                result.incorrectConcepts()
        ); }

    @Override
    @Transactional(readOnly = true)
    public EvaluationResponse getByAnswerId(UUID answerId) {
        Evaluation evaluation = evaluationRepository.findByAnswerId(answerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Evaluation for answer",
                                answerId
                        ));

        return toResponse(
                evaluation,
                evaluation.getMissingConcepts(),
                evaluation.getIncorrectConcepts()
        ); }

    private EvaluationResult parse(String response) {
        try {
            return objectMapper.readValue(extractJson(response), EvaluationResult.class);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("The AI returned an invalid evaluation payload", exception);
        }
    }

    private void validateScores(EvaluationResult result) {
        if (result == null || !isScore(result.correctnessScore()) || !isScore(result.completenessScore()) ||
                !isScore(result.relevanceScore()) || !isScore(result.clarityScore()) ||
                result.correctAnswer() == null || result.correctAnswer().isBlank() || result.feedback() == null || result.feedback().isBlank()) {
            log.warn("event=EVALUATION_INVALID_AI_RESULT resultPresent={}", result != null);
            throw new IllegalStateException("The AI returned an invalid evaluation");
        }
    }

    private boolean isScore(Integer value) { return value != null && value >= 0 && value <= 100; }
    private BigDecimal score(Integer value) { return BigDecimal.valueOf(value); }
    private String context(List<DocumentChunk> chunks) {
        return chunks.stream().map(chunk -> "[page %d, chunk %d] %s".formatted(chunk.pageNumber(), chunk.chunkIndex(), chunk.content()))
                .collect(java.util.stream.Collectors.joining("\n\n"));
    }
    private String toJson(List<String> concepts) {
        try { return objectMapper.writeValueAsString(concepts == null ? List.of() : concepts); }
        catch (JsonProcessingException exception) { throw new IllegalStateException("Unable to serialize concepts", exception); }
    }
    private List<String> readConcepts(String json) {
        try { return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)); }
        catch (JsonProcessingException exception) { throw new IllegalStateException("Stored evaluation concepts are invalid", exception); }
    }
    private EvaluationResponse toResponse(Evaluation evaluation, List<String> missing, List<String> incorrect) {
        return new EvaluationResponse(evaluation.getId(), evaluation.getAnswerId(), evaluation.getOverallScore(),
                evaluation.getCorrectnessScore(), evaluation.getCompletenessScore(), evaluation.getRelevanceScore(),
                evaluation.getClarityScore(), evaluation.getFeedback(), evaluation.getCorrectAnswer(), missing, incorrect,
                evaluation.getCreatedAt());
    }
    private String extractJson(String content) {
        String trimmed = content.trim();
        if (!trimmed.startsWith("```")) return trimmed;
        int firstNewline = trimmed.indexOf('\n');
        int closingFence = trimmed.lastIndexOf("```");
        return firstNewline > 0 && closingFence > firstNewline ? trimmed.substring(firstNewline + 1, closingFence).trim() : trimmed;
    }
}
