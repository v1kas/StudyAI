package com.vikas.studyai.rag.service;

import com.vikas.studyai.document.model.PageContent;
import com.vikas.studyai.rag.model.DocumentChunk;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ParagraphChunkingService implements ChunkingService {
    static final int CHUNK_SIZE = 1_000;
    static final int OVERLAP_SIZE = 150;

    @Override
    public List<DocumentChunk> chunk(UUID documentId, List<PageContent> pages) {
        List<DocumentChunk> chunks = new ArrayList<>();
        int chunkIndex = 0;
        for (PageContent page : pages) {
            for (String chunkContent : splitPage(page.content())) {
                chunks.add(new DocumentChunk(null, documentId, page.pageNumber(), chunkIndex++, chunkContent));
            }
        }
        return List.copyOf(chunks);
    }

    private List<String> splitPage(String content) {
        List<String> chunks = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        for (String paragraph : content.trim().split("\\n\\s*\\n")) {
            for (String part : splitLongParagraph(paragraph.trim())) {
                if (current.length() > 0 && current.length() + part.length() + 1 > CHUNK_SIZE) {
                    chunks.add(current.toString().trim());
                    current = new StringBuilder(trailingWords(current.toString()));
                }
                if (current.length() > 0) {
                    current.append(' ');
                }
                current.append(part);
            }
        }
        if (current.length() > 0) {
            chunks.add(current.toString().trim());
        }
        return chunks;
    }

    private List<String> splitLongParagraph(String paragraph) {
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        for (String word : paragraph.split("\\s+")) {
            if (current.length() > 0 && current.length() + word.length() + 1 > CHUNK_SIZE) {
                parts.add(current.toString());
                current = new StringBuilder(trailingWords(current.toString()));
            }
            if (current.length() > 0) {
                current.append(' ');
            }
            current.append(word);
        }
        if (current.length() > 0) {
            parts.add(current.toString());
        }
        return parts;
    }

    private String trailingWords(String value) {
        if (value.length() <= OVERLAP_SIZE) {
            return value;
        }
        int start = value.lastIndexOf(' ', value.length() - OVERLAP_SIZE);
        return start < 0 ? value.substring(value.length() - OVERLAP_SIZE) : value.substring(start + 1);
    }
}
