package com.vikas.studyai.answer.repository;

import com.vikas.studyai.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Collection;
import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, UUID> {
    List<Answer> findByQuestionIdIn(Collection<UUID> questionIds);
}
