package com.vikas.studyai.rag.embedding;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SpringAiEmbeddingService implements EmbeddingService {
    private final EmbeddingModel embeddingModel;

    public SpringAiEmbeddingService(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @Override
    public List<Double> embed(String text) {float[] embedding = embeddingModel.embed(text);

        List<Double> result = new ArrayList<>(embedding.length);

        for (float value : embedding) {
            result.add((double) value);
        }

        return result;
    }
}
