package com.vikas.studyai.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiTimingInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(ApiTimingInterceptor.class);
    private static final String START_NANOS = ApiTimingInterceptor.class.getName() + ".startNanos";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(START_NANOS, System.nanoTime());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception exception) {
        Object started = request.getAttribute(START_NANOS);
        if (started instanceof Long startNanos) {
            long elapsedMillis = (System.nanoTime() - startNanos) / 1_000_000;
            log.info("event=API_REQUEST method={} path={} status={} durationMs={}",
                    request.getMethod(), request.getRequestURI(), response.getStatus(), elapsedMillis);
        }
    }
}
