package com.vikas.studyai.common.exception;

import com.vikas.studyai.common.response.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiErrorResponse.of(404, "RESOURCE_NOT_FOUND", exception.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(InvalidDocumentException.class)
    ResponseEntity<ApiErrorResponse> handleInvalidDocument(InvalidDocumentException exception, HttpServletRequest request) {
        return ResponseEntity.badRequest().body(ApiErrorResponse.of(400, "INVALID_DOCUMENT", exception.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(AiServiceException.class)
    ResponseEntity<ApiErrorResponse> handleAiFailure(AiServiceException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(ApiErrorResponse.of(502, "AI_SERVICE_ERROR", "The AI service is currently unavailable", request.getRequestURI()));
    }

    @ExceptionHandler(VectorSearchException.class)
    ResponseEntity<ApiErrorResponse> handleVectorFailure(VectorSearchException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ApiErrorResponse.of(503, "VECTOR_SEARCH_ERROR", "Vector search is currently unavailable", request.getRequestURI()));
    }

    @ExceptionHandler(DocumentProcessingException.class)
    ResponseEntity<ApiErrorResponse> handleProcessingFailure(DocumentProcessingException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiErrorResponse.of(500, "DOCUMENT_PROCESSING_ERROR", exception.getMessage(), request.getRequestURI()));
    }
}
