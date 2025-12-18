package com.example.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        
        return new OpenAPI()
                // Servers configuration
                .servers(List.of(
                        new Server()
                                .url("https://9082.pro604cr.amypo.ai/")
                                .description("Production Server"),
                        new Server()
                                .url("http://localhost:8080/")
                                .description("Local Development Server")
                ))
                
                // API Information
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
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("API Support")
                                .email("support@example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                
                // Security Scheme
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT Authorization header using the Bearer scheme. Example: `Authorization: Bearer {token}`")));
    }
}