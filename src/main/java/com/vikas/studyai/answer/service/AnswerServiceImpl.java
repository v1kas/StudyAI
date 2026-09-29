package com.vikas.studyai.answer.service;

import com.vikas.studyai.answer.dto.AnswerResponse;
import com.vikas.studyai.answer.dto.SubmitAnswerRequest;
import com.vikas.studyai.answer.entity.Answer;
import com.vikas.studyai.answer.repository.AnswerRepository;
import com.vikas.studyai.common.exception.ResourceNotFoundException;
import com.vikas.studyai.evaluation.service.EvaluationService;
import com.vikas.studyai.question.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final EvaluationService evaluationService;

    public AnswerServiceImpl(AnswerRepository answerRepository, QuestionRepository questionRepository,
                             EvaluationService evaluationService) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.evaluationService = evaluationService;
    }

    @Override
    @Transactional
    public AnswerResponse submit(UUID questionId, SubmitAnswerRequest request) {
        if (!questionRepository.existsById(questionId)) throw new ResourceNotFoundException("Question", questionId);
        Answer answer = answerRepository.save(new Answer(questionId, request.answer()));
        evaluationService.evaluate(answer.getId());
        return toResponse(answer);
    }

    @Override
    @Transactional(readOnly = true)
    public AnswerResponse getById(UUID id) {
        return toResponse(answerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Answer", id)));
    }

    private AnswerResponse toResponse(Answer answer) {
        return new AnswerResponse(answer.getId(), answer.getQuestionId(), answer.getUserAnswer(), answer.getSubmittedAt());
    }
}
