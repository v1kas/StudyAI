package com.vikas.studyai.rag.embedding;

import java.util.List;

public interface EmbeddingService {
    List<Double> embed(String text);
}
