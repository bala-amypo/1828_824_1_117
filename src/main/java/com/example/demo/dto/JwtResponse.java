package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "JWT response DTO")
public class JwtResponse {
    
    @Schema(description = "JWT token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @Schema(description = "User ID", example = "1")
    private Long userId;
    
    @Schema(description = "Email", example = "admin@example.com")
    private String email;
    
    @Schema(description = "Role", example = "ADMIN")
    private String role;
}