package com.portfolio.taskmanager.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI baseOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Task Manager API")
                        .version("v1")
                        .description("API RESTful para gerenciamento de tarefas"));
    }
}
