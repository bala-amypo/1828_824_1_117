package com.example.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        
        return new OpenAPI()
                .info(new Info()
                        .title("IT Policy Violation Detection API")
                        .description("""
                            ### API Documentation
                            
                            This system captures and analyzes user login activity, device usage, and security events 
                            to detect potential IT policy violations across an organization.
                            
                            ### Features:
                            - User authentication and authorization with JWT
                            - Login event tracking and analysis
                            - Device profile management
                            - Configurable policy rules
                            - Violation detection and reporting
                            - Role-based access control (ADMIN, AUDITOR, USER)
                            
                            ### Authentication:
                            - Register at `/auth/register`
                            - Login at `/auth/login` to get JWT token
                            - Include token in header: `Authorization: Bearer <token>`
                            """)
                        .version("1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}