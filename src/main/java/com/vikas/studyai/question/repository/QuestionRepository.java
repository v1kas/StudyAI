package com.vikas.studyai.question.repository;

import com.vikas.studyai.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
    List<Question> findByDocumentId(UUID documentId);
}
