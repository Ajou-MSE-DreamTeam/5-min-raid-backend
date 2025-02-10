package com.momong.five_min_raid.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
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
                        .title("5 Minute Raid API Docs")
                        .description("Team Momong의 5분 레이드 API 명세서")
                        .version(appVersion))
                .externalDocs(new ExternalDocumentation()
                        .description("Github organization of team MoMong")
                        .url("https://github.com/Ajou-MSE-DreamTeam"))
                .components(new Components().addSecuritySchemes(
                        "access-token",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("Bearer").bearerFormat("JWT")
                ));
    }

    @Bean
    public GroupedOpenApi groupedOpenApiVersion1() {
        return GroupedOpenApi.builder()
                .group("v1")
                .packagesToScan("com.momong.five_min_raid")
                .pathsToMatch("/api/v1/**")
                .build();
    }
}
