package com.example.demo.security;
import io.jsonwebtoken.*;
import java.util.Date;

public class JwtUtil {
    private String secret;
    private long validity;

    public JwtUtil(String secret, long validity, boolean testMode) {
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

    public String getSubject(String t) { return getClaims(t).getSubject(); }
    public String getEmail(String t) { return getClaims(t).get("email", String.class); }
    public String getRole(String t) { return getClaims(t).get("role", String.class); }
    public Long getUserId(String t) { return getClaims(t).get("userId", Long.class); }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}