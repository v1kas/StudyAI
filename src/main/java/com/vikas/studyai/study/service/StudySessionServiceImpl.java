package com.vikas.studyai.study.service;

import com.vikas.studyai.answer.entity.Answer;
import com.vikas.studyai.answer.repository.AnswerRepository;
import com.vikas.studyai.common.exception.ResourceNotFoundException;
import com.vikas.studyai.document.repository.DocumentRepository;
import com.vikas.studyai.evaluation.entity.Evaluation;
import com.vikas.studyai.evaluation.repository.EvaluationRepository;
import com.vikas.studyai.question.entity.Question;
import com.vikas.studyai.question.repository.QuestionRepository;
import com.vikas.studyai.study.dto.CreateStudySessionRequest;
import com.vikas.studyai.study.dto.StudySessionResponse;
import com.vikas.studyai.study.dto.StudySummaryResponse;
import com.vikas.studyai.study.entity.StudySession;
import com.vikas.studyai.study.repository.StudySessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
public class StudySessionServiceImpl implements StudySessionService {
    private final StudySessionRepository sessionRepository;
    private final DocumentRepository documentRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final EvaluationRepository evaluationRepository;

    public StudySessionServiceImpl(StudySessionRepository sessionRepository, DocumentRepository documentRepository,
                                   QuestionRepository questionRepository, AnswerRepository answerRepository,
                                   EvaluationRepository evaluationRepository) {
        this.sessionRepository = sessionRepository; this.documentRepository = documentRepository;
        this.questionRepository = questionRepository; this.answerRepository = answerRepository;
        this.evaluationRepository = evaluationRepository;
    }

    @Override @Transactional
    public StudySessionResponse create(CreateStudySessionRequest request) {
        if (!documentRepository.existsById(request.documentId())) throw new ResourceNotFoundException("Document", request.documentId());
        return summarize(sessionRepository.save(new StudySession(request.documentId())));
    }

    @Override @Transactional(readOnly = true)
    public StudySessionResponse getById(UUID id) {
        return summarize(sessionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Study session", id)));
    }

    @Override @Transactional(readOnly = true)
    public StudySummaryResponse getSummaryByDocumentId(UUID documentId) {
        if (!documentRepository.existsById(documentId)) throw new ResourceNotFoundException("Document", documentId);
        Summary summary = summarizeDocument(documentId);
        return new StudySummaryResponse(documentId, summary.totalQuestions(), summary.answered(), summary.averageScore(), List.of(), List.of());
    }

    private StudySessionResponse summarize(StudySession session) {
        Summary summary = summarizeDocument(session.getDocumentId());
        return new StudySessionResponse(session.getId(), session.getDocumentId(), summary.totalQuestions(), summary.answered(),
                summary.averageScore(), List.of(), List.of(), session.getStartedAt(), session.getCompletedAt());
    }

    private Summary summarizeDocument(UUID documentId) {
        List<Question> questions = questionRepository.findByDocumentId(documentId);
        if (questions.isEmpty()) return new Summary(0, 0, BigDecimal.ZERO);
        List<Answer> answers = answerRepository.findByQuestionIdIn(questions.stream().map(Question::getId).toList());
        List<Evaluation> evaluations = answers.isEmpty() ? List.of()
                : evaluationRepository.findByAnswerIdIn(answers.stream().map(Answer::getId).toList());
        BigDecimal average = evaluations.isEmpty() ? BigDecimal.ZERO : evaluations.stream().map(Evaluation::getOverallScore)
                .reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(evaluations.size()), 2, RoundingMode.HALF_UP);
        int answered = (int) answers.stream().map(Answer::getQuestionId).distinct().count();
        return new Summary(questions.size(), answered, average);
    }

    private record Summary(int totalQuestions, int answered, BigDecimal averageScore) { }
}
