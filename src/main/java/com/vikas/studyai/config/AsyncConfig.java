package com.vikas.studyai.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableAsync
public class AsyncConfig implements WebMvcConfigurer {
    private final ApiTimingInterceptor apiTimingInterceptor;
    private final String frontendOrigin;

    public AsyncConfig(ApiTimingInterceptor apiTimingInterceptor,
                       @Value("${study-ai.frontend.allowed-origin:http://localhost:5173}") String frontendOrigin) {
        this.apiTimingInterceptor = apiTimingInterceptor;
        this.frontendOrigin = frontendOrigin;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiTimingInterceptor);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(frontendOrigin, "http://localhost:5173", "http://127.0.0.1:5173")
                .allowedMethods("GET", "POST", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
