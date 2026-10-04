package com.vikas.studyai.answer.service;

import com.vikas.studyai.answer.dto.AnswerResponse;
import com.vikas.studyai.answer.dto.SubmitAnswerRequest;
import java.util.UUID;
import java.util.List;

public interface AnswerService {
    AnswerResponse submit(UUID questionId, SubmitAnswerRequest request);
    AnswerResponse getById(UUID id);
    List<AnswerResponse> getRecent(int limit);
}
