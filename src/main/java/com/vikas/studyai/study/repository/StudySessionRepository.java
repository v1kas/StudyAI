package com.vikas.studyai.study.repository;

import com.vikas.studyai.study.entity.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface StudySessionRepository extends JpaRepository<StudySession, UUID> { }
