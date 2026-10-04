package com.vikas.studyai.answer.service;

import com.vikas.studyai.answer.dto.AnswerResponse;
import com.vikas.studyai.answer.dto.SubmitAnswerRequest;
import com.vikas.studyai.answer.entity.Answer;
import com.vikas.studyai.answer.repository.AnswerRepository;
import com.vikas.studyai.common.exception.ResourceNotFoundException;
import com.vikas.studyai.question.repository.QuestionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    public AnswerServiceImpl(AnswerRepository answerRepository, QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public AnswerResponse submit(UUID questionId, SubmitAnswerRequest request) {
        if (!questionRepository.existsById(questionId)) throw new ResourceNotFoundException("Question", questionId);
        Answer answer = answerRepository.save(new Answer(questionId, request.answer()));
        return toResponse(answer);
    }

    @Override
    public AnswerResponse getById(UUID id) {
        return toResponse(answerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Answer", id)));
    }

    @Override
    public java.util.List<AnswerResponse> getRecent(int limit) {
        if (limit < 1 || limit > 50) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "limit must be between 1 and 50");
        }
        return answerRepository.findAllByOrderBySubmittedAtDesc(PageRequest.of(0, limit))
                .stream().map(this::toResponse).toList();
    }

    private AnswerResponse toResponse(Answer answer) {
        return new AnswerResponse(answer.getId(), answer.getQuestionId(), answer.getUserAnswer(), answer.getSubmittedAt());
    }
}
