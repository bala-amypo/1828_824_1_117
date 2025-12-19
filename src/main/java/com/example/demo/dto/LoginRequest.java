package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Login request DTO")
public class LoginRequest {
    
    @Schema(description = "Username or email", example = "admin", required = true)
    private String usernameOrEmail;
    
    @Schema(description = "Password", example = "admin123", required = true)
    private String password;
}