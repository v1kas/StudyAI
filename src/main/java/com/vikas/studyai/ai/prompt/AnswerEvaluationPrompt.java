package com.vikas.studyai.ai.prompt;

public final class AnswerEvaluationPrompt {
    private AnswerEvaluationPrompt() { }

    public static String build(String question, String expectedAnswer, String context, String answer) {
        return """
                You are an educational answer evaluator. Grade the student's answer fairly and give credit for correct ideas expressed in different words.

                QUESTION:
                %s

                EXPECTED ANSWER / GRADING REFERENCE:
                %s

                REFERENCE CONTEXT:
                %s

                STUDENT ANSWER:
                %s

                SCORE EACH DIMENSION INDEPENDENTLY from 0 to 100:
                - correctnessScore: factual accuracy compared with the expected answer and source material.
                - completenessScore: how many important parts of the expected answer are covered.
                - relevanceScore: how directly the response answers the question.
                - clarityScore: how understandable and well organized the response is.

                A score of 0 means the response is blank, wholly incorrect, or contains no relevant information.
                Do not give zero merely because the response is concise. A correct concise answer should receive strong correctness and relevance scores.
                Grade each factual claim separately. If the answer contains the correct answer plus an extra incorrect or irrelevant claim, give credit for the correct answer and deduct only for the extra claim; do not describe the whole answer as incorrect. When the core answer is correct, correctness and relevance should normally be at least 70, even if a minor extra detail is wrong. List the extra error in incorrectConcepts and explain the deduction in feedback.
                Score the four dimensions independently. Do not return the same score for every dimension unless the answer genuinely merits equal scores in all four.
                Do not copy placeholder or example scores. Assess the student's answer itself.
                Use the expected answer as the rubric and the reference context to verify facts. If they differ, follow the reference context.

                Return exactly one valid JSON object, with no markdown fences or extra text, using this schema:
                {"correctnessScore": <integer 0-100>, "completenessScore": <integer 0-100>,
                "relevanceScore": <integer 0-100>, "clarityScore": <integer 0-100>,
                "missingConcepts": ["concept"], "incorrectConcepts": ["concept"],
                "correctAnswer": "concise accurate answer", "feedback": "specific feedback"}
                """.formatted(question, expectedAnswer, context, answer);
    }
}
