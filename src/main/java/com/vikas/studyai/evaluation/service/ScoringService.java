package com.vikas.studyai.evaluation.service;

import com.vikas.studyai.evaluation.dto.EvaluationResult;
import java.math.BigDecimal;

public interface ScoringService {
    BigDecimal calculateOverallScore(EvaluationResult result);
}
