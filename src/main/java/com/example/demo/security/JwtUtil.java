package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    private String secret = "your_secret_key_here"; // Use the one from your config
    private long validityInMs = 3600000; // 1 hour

    // Required Constructor by Technical Constraints
    public JwtUtil() {}
    
    public JwtUtil(String secret, long validityInMs, boolean isTestMode) {
        this.secret = secret;
        this.validityInMs = validityInMs;
    }

    // --- FIX: Add these methods to resolve "cannot find symbol" errors ---

    public String getEmail(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getRole(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return (String) claims.get("role");
    }

    public Long getUserId(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        // Ensure you handle the numeric conversion safely
        Object userId = claims.get("userId");
        if (userId instanceof Integer) return ((Integer) userId).longValue();
        if (userId instanceof Long) return (Long) userId;
        return null;
    }

    // --- Helper Methods ---

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
    
    // ... include your generateToken and validateToken methods here
}