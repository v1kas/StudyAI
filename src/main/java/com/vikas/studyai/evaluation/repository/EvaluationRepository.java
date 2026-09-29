package com.vikas.studyai.evaluation.repository;

import com.vikas.studyai.evaluation.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface EvaluationRepository extends JpaRepository<Evaluation, UUID> {
    Optional<Evaluation> findByAnswerId(UUID answerId);
    List<Evaluation> findByAnswerIdIn(Collection<UUID> answerIds);
}
