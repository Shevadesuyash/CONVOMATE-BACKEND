package com.net.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI swaggerUISetup() {
        return new OpenAPI().info(
                new Info()
                        .title("Backend Service - Convoment")
                        .description("Sample project for used for training purpose")
        );
    }

}