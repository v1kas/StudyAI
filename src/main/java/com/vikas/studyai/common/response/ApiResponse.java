package com.vikas.studyai.common.response;

import java.time.Instant;

/** Standard envelope for REST responses. */
public record ApiResponse<T>(T data, String message, Instant timestamp) {

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(data, message, Instant.now());
    }
}
