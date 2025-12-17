package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private final String type = "Bearer";

    // Constructor for just the token
    public JwtResponse(String accessToken) {
        this.token = accessToken;
    }
}