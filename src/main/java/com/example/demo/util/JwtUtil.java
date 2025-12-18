package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    
    private String secret;
    private long validityInMs;
    private boolean isTestMode;
    
    // EXACT CONSTRUCTOR SIGNATURE AS REQUIRED
    public JwtUtil(@Value("${jwt.secret}") String secret, 
                   @Value("${jwt.validity}") long validityInMs, 
                   @Value("${jwt.test-mode}") boolean isTestMode) {
        this.secret = secret;
        this.validityInMs = validityInMs;
        this.isTestMode = isTestMode;
    }
    
    // Default constructor for Spring
    public JwtUtil() {
    }
    
    public String generateToken(String subject, Long userId, String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", email);
        claims.put("role", role);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityInMs))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getEmail(String token) {
        return getClaimsFromToken(token).get("email", String.class);
    }
    
    public String getRole(String token) {
        return getClaimsFromToken(token).get("role", String.class);
    }
    
    public Long getUserId(String token) {
        return getClaimsFromToken(token).get("userId", Long.class);
    }
    
    public String getSubject(String token) {
        return getClaimsFromToken(token).getSubject();
    }
    
    // CHANGE FROM PRIVATE TO PUBLIC
    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}