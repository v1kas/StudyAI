package com.vikas.studyai.ai.prompt;

import com.vikas.studyai.question.entity.Difficulty;
import com.vikas.studyai.question.entity.QuestionType;
import java.util.List;

public final class QuestionGenerationPrompt {
    private QuestionGenerationPrompt() { }

    public static String build(String context, int numberOfQuestions, String difficulty, List<QuestionType> types) {
        return """
                You create high-quality study questions using only the supplied context.
                Generate exactly %d questions. Requested difficulty: %s. Allowed types: %s.

                CONTEXT:
                %s

                Return JSON only, with this shape:
                {"questions":[{"question":"...","type":"CONCEPTUAL","difficulty":"EASY",
                "expectedAnswer":"...","sourceChunkIds":["uuid"]}]}
                Every sourceChunkId must identify a chunk in the supplied context.
                """.formatted(numberOfQuestions, difficulty, types, context);
    }
}
