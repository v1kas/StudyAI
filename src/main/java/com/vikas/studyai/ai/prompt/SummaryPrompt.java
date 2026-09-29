package com.vikas.studyai.ai.prompt;

public final class SummaryPrompt {
    private SummaryPrompt() { }

    public static String build(String context) {
        return "Summarize the following study material accurately and concisely:\n\n" + context;
    }
}
