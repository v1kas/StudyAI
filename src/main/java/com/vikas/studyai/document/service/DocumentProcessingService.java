package com.vikas.studyai.document.service;

import com.vikas.studyai.rag.service.RagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class DocumentProcessingService {
    private static final Logger log = LoggerFactory.getLogger(DocumentProcessingService.class);
    private final RagService ragService;

    public DocumentProcessingService(RagService ragService) { this.ragService = ragService; }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void process(DocumentUploadedEvent event) {
        log.info("event=DOCUMENT_PROCESSING_STARTED documentId={}", event.documentId());
        ragService.indexDocument(event.documentId());
        log.info("event=DOCUMENT_PROCESSING_FINISHED documentId={}", event.documentId());
    }
}
