package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String employeeId;
    private String username;
    private String password;
    private String email;
    private String role; // Optional: Defaults to USER in controller if null
}