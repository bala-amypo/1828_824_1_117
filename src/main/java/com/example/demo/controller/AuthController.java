package com.example.demo.controller;

import com.example.demo.dto.JwtResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.UserAccount;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.UserAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Authentication", description = "Authentication endpoints")
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserAccountService userAccountService;
    private final PasswordEncoder passwordEncoder;
    
    public AuthController(AuthenticationManager authenticationManager,
                         JwtUtil jwtUtil,
                         UserAccountService userAccountService,
                         PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userAccountService = userAccountService;
        this.passwordEncoder = passwordEncoder;
    }
    
    // ==================== HEALTH ENDPOINTS ====================
    
    @Operation(summary = "API Health Check", description = "Check if API is running")
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "IT Policy Violation Detection API is running");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "API Home", description = "Welcome endpoint")
    @GetMapping("/")
    public ResponseEntity<Map<String, String>> home() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to IT Policy Violation Detection API");
        response.put("version", "1.0.0");
        response.put("documentation", "/swagger-ui/index.html");
        response.put("health", "/auth/health");
        response.put("status", "/status");
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "API Info", description = "Get API information")
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("name", "IT Policy Violation Detection API");
        response.put("description", "Captures and analyzes user login activity to detect IT policy violations");
        response.put("version", "1.0.0");
        response.put("author", "System Administrator");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("register", "POST /auth/register");
        endpoints.put("login", "POST /auth/login");
        endpoints.put("users", "GET /api/users");
        endpoints.put("logins", "POST /api/logins/record");
        endpoints.put("devices", "GET /api/devices");
        endpoints.put("rules", "GET /api/rules");
        endpoints.put("violations", "GET /api/violations");
        
        response.put("endpoints", endpoints);
        return ResponseEntity.ok(response);
    }
    
    // ==================== AUTH ENDPOINTS ====================
    
    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // Check if username already exists
            Optional<UserAccount> existingUser = userAccountService.findByUsername(request.getUsername());
            if (existingUser.isPresent()) {
                return ResponseEntity.badRequest().body("Username already exists");
            }
            
            // Create new user
            UserAccount user = new UserAccount();
            user.setEmployeeId(request.getEmployeeId());
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setRole(request.getRole() != null ? request.getRole() : "USER");
            user.setStatus("ACTIVE");
            
            UserAccount createdUser = userAccountService.createUser(user);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("userId", createdUser.getId());
            response.put("username", createdUser.getUsername());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }
    
    @Operation(summary = "Login user")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Find user by username
            Optional<UserAccount> userOpt = userAccountService.findByUsername(request.getUsernameOrEmail());
            if (userOpt.isEmpty()) {
                // Try by email
                // Note: You need to add findByEmail method to UserAccountService
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
            
            UserAccount user = userOpt.get();
            
            // Authenticate
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    request.getPassword()
                )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Generate token
            String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getId(),
                user.getEmail(),
                user.getRole()
            );
            
            JwtResponse response = new JwtResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getRole()
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}