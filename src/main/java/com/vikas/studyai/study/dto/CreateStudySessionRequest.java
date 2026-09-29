package com.vikas.studyai.study.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateStudySessionRequest(@NotNull UUID documentId) { }
