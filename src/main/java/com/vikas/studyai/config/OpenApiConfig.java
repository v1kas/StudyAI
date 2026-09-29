package com.vikas.studyai.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI studyAiOpenApi() {
        return new OpenAPI().info(new Info().title("StudyAI API").version("v1"));
    }
}
