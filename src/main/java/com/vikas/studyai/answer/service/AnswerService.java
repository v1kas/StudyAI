package com.vikas.studyai.answer.service;

import com.vikas.studyai.answer.dto.AnswerResponse;
import com.vikas.studyai.answer.dto.SubmitAnswerRequest;
import java.util.UUID;

public interface AnswerService {
    AnswerResponse submit(UUID questionId, SubmitAnswerRequest request);
    AnswerResponse getById(UUID id);
}
