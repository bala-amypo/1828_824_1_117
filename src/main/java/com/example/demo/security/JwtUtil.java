package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    private String secret = "TestSecretKeyForJWT1234567890"; 
    private long validityInMs = 3600000; // 1 hour

    // Required Constructor for Spring DI
    public JwtUtil() {}
    
    // Required Constructor by Master Test Suite (Step 0.4)
    public JwtUtil(String secret, long validityInMs, boolean isTestMode) {
        this.secret = secret;
        this.validityInMs = validityInMs;
    }

    // --- TOKEN GENERATION ---
    public String generateToken(String username, Long userId, String email, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("email", email)
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validityInMs))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    // --- VALIDATION ---
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // --- EXTRACTION METHODS (Resolves "cannot find symbol" errors) ---

    public String getSubject(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getEmail(String token) {
        // The test suite expects the "email" claim specifically
        final Claims claims = getAllClaimsFromToken(token);
        return claims.get("email", String.class);
    }

    public String getRole(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return claims.get("role", String.class);
    }

    public Long getUserId(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        Object userId = claims.get("userId");
        if (userId instanceof Integer) return ((Integer) userId).longValue();
        if (userId instanceof Long) return (Long) userId;
        return null;
    }

    // --- HELPER METHODS ---

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}