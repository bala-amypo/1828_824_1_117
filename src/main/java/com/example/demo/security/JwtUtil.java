package com.example.demo.security;
import io.jsonwebtoken.*;
import java.util.Date;

public class JwtUtil {
    private String secret;
    private long validity;

    public JwtUtil(String secret, long validity, boolean isTestMode) {
        this.secret = secret;
        this.validity = validity;
    }

    public String generateToken(String sub, Long userId, String email, String role) {
        return Jwts.builder()
                .setSubject(sub)
                .claim("userId", userId)
                .claim("email", email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) { return false; }
    }

    public String getEmail(String token) { return getClaims(token).get("email", String.class); }
    public String getRole(String token) { return getClaims(token).get("role", String.class); }
    public Long getUserId(String token) { return getClaims(token).get("userId", Long.class); }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}