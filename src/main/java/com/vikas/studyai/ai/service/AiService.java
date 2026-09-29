package com.vikas.studyai.ai.service;

/** Provider-independent gateway for text generation. */
public interface AiService {
    String generate(String prompt);
}
