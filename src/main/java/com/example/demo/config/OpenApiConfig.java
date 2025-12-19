package com.example.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
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
                .servers(List.of(
                        new Server()
                                .url("https://9082.pro604cr.amypo.ai")
                                .description("Production Server")
                ))
                .info(new Info()
                        .title("IT Policy Violation Detection API")
                        .description("""
                            ### API Documentation
                            
                            **Important:** Use the "Try it out" button above to test endpoints.
                            
                            ### Quick Start:
                            1. Register a user: `POST /auth/register`
                            2. Login: `POST /auth/login` (copy the token)
                            3. Click "Authorize" button (🔒) and paste: `Bearer YOUR_TOKEN`
                            4. Test protected endpoints
                            
                            ### Authentication:
                            - Register: `POST /auth/register`
                            - Login: `POST /auth/login`
                            - Token format: `Authorization: Bearer {token}`
                            """)
                        .version("1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT token in format: Bearer YOUR_TOKEN")));
    }
}