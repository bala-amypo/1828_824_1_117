package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Register request DTO")
public class RegisterRequest {
    
    @Schema(description = "Employee ID", example = "EMP001", required = true)
    private String employeeId;
    
    @Schema(description = "Username", example = "admin", required = true)
    private String username;
    
    @Schema(description = "Email", example = "admin@example.com", required = true)
    private String email;
    
    @Schema(description = "Password", example = "admin123", required = true)
    private String password;
    
    @Schema(description = "Role (ADMIN, USER, AUDITOR)", example = "ADMIN", required = false)
    private String role;
}