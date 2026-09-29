package com.vikas.studyai.document.repository;

import com.vikas.studyai.document.entity.StudyDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<StudyDocument, UUID> { }
