package com.vikas.studyai.evaluation.service;

import com.vikas.studyai.evaluation.dto.EvaluationResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class WeightedScoringService implements ScoringService {
    @Override
    public BigDecimal calculateOverallScore(EvaluationResult result) {
        return score(result.correctnessScore()).multiply(BigDecimal.valueOf(0.40))
                .add(score(result.completenessScore()).multiply(BigDecimal.valueOf(0.30)))
                .add(score(result.relevanceScore()).multiply(BigDecimal.valueOf(0.20)))
                .add(score(result.clarityScore()).multiply(BigDecimal.valueOf(0.10)))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal score(int value) { return BigDecimal.valueOf(value); }
}
