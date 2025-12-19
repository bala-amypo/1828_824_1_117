package com.example.demo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_accounts", uniqueConstraints = {
    @UniqueConstraint(columnNames = "employeeId"),
    @UniqueConstraint(columnNames = "username"),
    @UniqueConstraint(columnNames = "email")
})
@Schema(description = "User account entity")
public class UserAccount {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "User ID", example = "1")
    private Long id;
    
    @Column(unique = true, nullable = false)
    @Schema(description = "Employee ID", example = "EMP001", required = true)
    private String employeeId;
    
    @Column(unique = true, nullable = false)
    @Schema(description = "Username", example = "admin", required = true)
    private String username;
    
    @Column(unique = true)
    @Schema(description = "Email", example = "admin@example.com")
    private String email;
    
    @Column(nullable = false)
    @Schema(description = "Password (hashed)", example = "$2a$10$...", required = true)
    private String password;
    
    @Column(nullable = false)
    @Schema(description = "Role (ADMIN, USER, AUDITOR)", example = "ADMIN", required = true)
    private String role = "USER";
    
    @Column(nullable = false)
    @Schema(description = "Status (ACTIVE, SUSPENDED)", example = "ACTIVE", required = true)
    private String status = "ACTIVE";
    
    @Column(nullable = false, updatable = false)
    @Schema(description = "Creation timestamp", example = "2024-12-19T10:30:00")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}