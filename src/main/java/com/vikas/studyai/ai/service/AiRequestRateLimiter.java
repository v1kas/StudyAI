package com.vikas.studyai.ai.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.LockSupport;

/** Limits the start rate of requests sent to the configured AI provider. */
@Component
public class AiRequestRateLimiter {
    private static final Logger log = LoggerFactory.getLogger(AiRequestRateLimiter.class);
    private final long intervalNanos;
    private long nextPermitNanos;

    public AiRequestRateLimiter(@Value("${study-ai.ai.rate-limit.requests-per-second:2}") double requestsPerSecond) {
        if (requestsPerSecond <= 0) {
            throw new IllegalArgumentException("AI requests per second must be greater than zero");
        }
        this.intervalNanos = (long) (1_000_000_000d / requestsPerSecond);
    }

    public void acquire() {
        long permitNanos;
        synchronized (this) {
            long now = System.nanoTime();
            permitNanos = Math.max(now, nextPermitNanos);
            nextPermitNanos = permitNanos + intervalNanos;
        }

        long waitNanos = permitNanos - System.nanoTime();
        if (waitNanos > 0) {
            log.info("event=AI_RATE_LIMIT_WAIT waitMillis={}", waitNanos / 1_000_000);
            LockSupport.parkNanos(waitNanos);
            if (Thread.interrupted()) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Interrupted while waiting for AI request rate limit");
            }
        }
    }
}
