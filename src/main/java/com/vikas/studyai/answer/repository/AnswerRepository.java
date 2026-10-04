package com.vikas.studyai.answer.repository;

import com.vikas.studyai.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface AnswerRepository extends JpaRepository<Answer, UUID> {
    List<Answer> findByQuestionIdIn(Collection<UUID> questionIds);
    List<Answer> findAllByOrderBySubmittedAtDesc(Pageable pageable);
}
