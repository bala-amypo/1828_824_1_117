package com.example.demo.controller;

import com.example.demo.dto.JwtResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.UserAccount;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.UserAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Tag(name = "1. Authentication", description = "Authentication and health endpoints")
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
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "IT Policy Violation Detection API");
        response.put("timestamp", new Date());
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "API Home", description = "Welcome endpoint")
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to IT Policy Violation Detection API");
        response.put("description", "System for detecting IT policy violations");
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }
    
    // ==================== AUTH ENDPOINTS ====================
    
    @Operation(summary = "Register a new user", description = "Create a new user account")
    @PostMapping("/register")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered successfully",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Map.class),
            examples = @ExampleObject(value = "{\"message\": \"User registered successfully\", \"userId\": 1, \"username\": \"admin\"}"))),
        @ApiResponse(responseCode = "400", description = "Registration failed",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"error\": \"Username already exists\"}")))
    })
    public ResponseEntity<?> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Register request",
                required = true,
                content = @Content(
                    schema = @Schema(implementation = RegisterRequest.class),
                    examples = @ExampleObject(value = "{\"employeeId\": \"EMP001\", \"username\": \"admin\", \"email\": \"admin@example.com\", \"password\": \"admin123\", \"role\": \"ADMIN\"}")
                )
            )
            @RequestBody RegisterRequest request) {
        try {
            // Check if username already exists
            Optional<UserAccount> existingUser = userAccountService.findByUsername(request.getUsername());
            if (existingUser.isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Username already exists");
                return ResponseEntity.badRequest().body(error);
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
            Map<String, String> error = new HashMap<>();
            error.put("error", "Registration failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @Operation(summary = "Login user", description = "Authenticate user and return JWT token")
    @PostMapping("/login")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = JwtResponse.class))),
        @ApiResponse(responseCode = "401", description = "Invalid credentials",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"error\": \"Invalid username or password\"}")))
    })
    public ResponseEntity<?> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Login request",
                required = true,
                content = @Content(
                    schema = @Schema(implementation = LoginRequest.class),
                    examples = @ExampleObject(value = "{\"usernameOrEmail\": \"admin\", \"password\": \"admin123\"}")
                )
            )
            @RequestBody LoginRequest request) {
        try {
            // Find user by username
            Optional<UserAccount> userOpt = userAccountService.findByUsername(request.getUsernameOrEmail());
            
            if (userOpt.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid username or password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
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
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
}