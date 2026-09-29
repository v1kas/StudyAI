package com.vikas.studyai.evaluation.service;

import com.vikas.studyai.evaluation.dto.EvaluationResponse;
import java.util.UUID;

public interface EvaluationService {
    EvaluationResponse evaluate(UUID answerId);
    EvaluationResponse getByAnswerId(UUID answerId);
}
