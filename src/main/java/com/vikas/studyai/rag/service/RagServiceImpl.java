package com.vikas.studyai.rag.service;

import com.vikas.studyai.common.exception.ResourceNotFoundException;
import com.vikas.studyai.common.exception.DocumentProcessingException;
import com.vikas.studyai.document.entity.DocumentStatus;
import com.vikas.studyai.document.entity.StudyDocument;
import com.vikas.studyai.document.model.PageContent;
import com.vikas.studyai.document.repository.DocumentRepository;
import com.vikas.studyai.document.service.PdfService;
import com.vikas.studyai.rag.embedding.EmbeddingService;
import com.vikas.studyai.rag.model.DocumentChunk;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Service
public class RagServiceImpl implements RagService {
    private static final Logger log = LoggerFactory.getLogger(RagServiceImpl.class);
    private final DocumentRepository documentRepository;
    private final PdfService pdfService;
    private final ChunkingService chunkingService;
    private final EmbeddingService embeddingService;
    private final DocumentChunkStore chunkStore;

    public RagServiceImpl(DocumentRepository documentRepository, PdfService pdfService,
                          ChunkingService chunkingService, EmbeddingService embeddingService,
                          DocumentChunkStore chunkStore) {
        this.documentRepository = documentRepository;
        this.pdfService = pdfService;
        this.chunkingService = chunkingService;
        this.embeddingService = embeddingService;
        this.chunkStore = chunkStore;
    }

    @Override
    @Transactional(noRollbackFor = DocumentProcessingException.class)
    public void indexDocument(UUID documentId) {
        StudyDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document", documentId));
        document.setStatus(DocumentStatus.PROCESSING);
        document.setProcessingError(null);
        try {
            log.info("event=DOCUMENT_EXTRACTION documentId={}", documentId);
            List<PageContent> pages = pdfService.extract(Path.of(document.getFilePath()));
            List<DocumentChunk> chunks = chunkingService.chunk(documentId, pages);
            log.info("event=CHUNK_GENERATION documentId={} chunks={}", documentId, chunks.size());
            List<List<Double>> embeddings = chunks.stream()
                    .map(chunk -> embeddingService.embed(chunk.content()))
                    .toList();
            log.info("event=EMBEDDING_GENERATION documentId={} chunks={}", documentId, chunks.size());
            chunkStore.replaceForDocument(documentId, chunks, embeddings);
            document.setStatus(DocumentStatus.READY);
        } catch (RuntimeException exception) {
            document.setStatus(DocumentStatus.FAILED);
            document.setProcessingError(safeMessage(exception));
            log.error("event=DOCUMENT_PROCESSING_FAILED documentId={}", documentId, exception);
            throw new DocumentProcessingException("Document processing failed", exception);
        }
    }

    private String safeMessage(RuntimeException exception) {
        String message = exception.getMessage();
        return message == null ? "Document processing failed" : message.substring(0, Math.min(message.length(), 1_000));
    }
}
