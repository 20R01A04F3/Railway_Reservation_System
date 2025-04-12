package com.example.swagger;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .pathsToMatch("/**") // Adjust the paths you want to include
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
        		 .components(new Components()
                         .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                 .type(SecurityScheme.Type.HTTP)
                                 .scheme("bearer")
                                 .bearerFormat("JWT")))
                 .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}