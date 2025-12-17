package com.example.demo.dto;

public class JwtResponse {
    private String token;
    private String type = "Bearer";

    // Single Constructor - Fixes the "already defined" error
    public JwtResponse(String accessToken) {
        this.token = accessToken;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}