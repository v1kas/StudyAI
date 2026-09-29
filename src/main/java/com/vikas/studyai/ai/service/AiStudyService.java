package com.vikas.studyai.ai.service;

import com.vikas.studyai.ai.model.AnswerAssessment;
import com.vikas.studyai.ai.model.GeneratedQuestion;
import java.util.List;

public interface AiStudyService {
    List<GeneratedQuestion> generateQuestions(String context, int count);
    AnswerAssessment evaluateAnswer(String question, String answer, String context);
}
