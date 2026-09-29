package com.vikas.studyai.ai.prompt;

public final class AnswerEvaluationPrompt {
    private AnswerEvaluationPrompt() { }

    public static String build(String question, String context, String answer) {
        return """
                You are an educational answer evaluator. Evaluate the student's answer using ONLY the supplied reference context.

                QUESTION:
                %s

                REFERENCE CONTEXT:
                %s

                STUDENT ANSWER:
                %s

                Return JSON only with integer scores from 0 to 100:
                {"correctnessScore":0,"completenessScore":0,"relevanceScore":0,"clarityScore":0,
                "missingConcepts":["..."],"incorrectConcepts":["..."],"correctAnswer":"...","feedback":"..."}
                """.formatted(question, context, answer);
    }
}
