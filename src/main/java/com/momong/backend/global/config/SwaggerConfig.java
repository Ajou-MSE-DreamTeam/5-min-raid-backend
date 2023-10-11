package com.momong.backend.global.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApi(@Value("${backend.app.version}") String appVersion) {
        return new OpenAPI()
                .info(new Info()
                        .title("MoMong API Docs")
                        .description("Team Momong의 API 명세서")
                        .version(appVersion))
                .externalDocs(new ExternalDocumentation()
                        .description("Github organization of team MoMong")
                        .url("https://github.com/Ajou-MSE-DreamTeam"));
    }

    @Bean
    public GroupedOpenApi groupedOpenApiVersion1() {
        return GroupedOpenApi.builder()
                .group("v1")
                .packagesToScan("com.momong.backend")
                .pathsToMatch("/api/v1/**")
                .build();
    }
}
