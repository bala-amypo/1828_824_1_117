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
    
    // ==================== HEALTH & INFO ENDPOINTS ====================
    
    @Operation(summary = "API Health Check", description = "Check if API is running")
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "IT Policy Violation Detection API");
        response.put("timestamp", new Date());
        response.put("version", "1.0.0");
        response.put("message", "API is running successfully");
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "API Home", description = "Welcome endpoint")
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to IT Policy Violation Detection API");
        response.put("description", "System for detecting IT policy violations through login activity analysis");
        response.put("version", "1.0.0");
        response.put("status", "operational");
        response.put("timestamp", new Date());
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("register", "POST /auth/register");
        endpoints.put("login", "POST /auth/login");
        endpoints.put("health", "GET /auth/health");
        endpoints.put("info", "GET /auth/info");
        endpoints.put("swagger", "/swagger-ui/index.html");
        endpoints.put("status", "/status");
        response.put("endpoints", endpoints);
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Get API information", description = "Detailed API information and endpoints")
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getApiInfo() {
        Map<String, Object> info = new HashMap<>();
        
        // Basic API Info
        info.put("name", "IT Policy Violation Detection API");
        info.put("description", "Captures and analyzes user login activity, device usage, and security events to detect potential IT policy violations");
        info.put("version", "1.0.0");
        info.put("status", "active");
        info.put("timestamp", new Date());
        
        // Authentication Info
        Map<String, String> authInfo = new HashMap<>();
        authInfo.put("method", "JWT Bearer Token");
        authInfo.put("register_endpoint", "POST /auth/register");
        authInfo.put("login_endpoint", "POST /auth/login");
        authInfo.put("token_format", "Bearer {token}");
        info.put("authentication", authInfo);
        
        // API Endpoints Structure
        Map<String, Object> endpoints = new HashMap<>();
        
        // Authentication Endpoints
        Map<String, String> authEndpoints = new HashMap<>();
        authEndpoints.put("register", "POST /auth/register - Register new user");
        authEndpoints.put("login", "POST /auth/login - Login and get JWT token");
        authEndpoints.put("health", "GET /auth/health - Health check");
        authEndpoints.put("info", "GET /auth/info - API information");
        endpoints.put("authentication", authEndpoints);
        
        // User Management Endpoints
        Map<String, String> userEndpoints = new HashMap<>();
        userEndpoints.put("create_user", "POST /api/users - Create new user (ADMIN only)");
        userEndpoints.put("get_user", "GET /api/users/{id} - Get user by ID");
        userEndpoints.put("update_status", "PUT /api/users/{id}/status - Update account status");
        userEndpoints.put("list_users", "GET /api/users - List all users");
        endpoints.put("user_management", userEndpoints);
        
        // Login Events Endpoints
        Map<String, String> loginEndpoints = new HashMap<>();
        loginEndpoints.put("record_login", "POST /api/logins/record - Record login attempt");
        loginEndpoints.put("get_user_logins", "GET /api/logins/user/{userId} - Get events for user");
        loginEndpoints.put("get_suspicious", "GET /api/logins/suspicious/{userId} - Get suspicious logins");
        loginEndpoints.put("list_all_logins", "GET /api/logins - List all login events");
        endpoints.put("login_events", loginEndpoints);
        
        // Device Management Endpoints
        Map<String, String> deviceEndpoints = new HashMap<>();
        deviceEndpoints.put("register_device", "POST /api/devices - Register device");
        deviceEndpoints.put("update_trust", "PUT /api/devices/{id}/trust - Update trusted status");
        deviceEndpoints.put("get_user_devices", "GET /api/devices/user/{userId} - List devices by user");
        deviceEndpoints.put("lookup_device", "GET /api/devices/lookup/{deviceId} - Lookup device by ID");
        endpoints.put("device_management", deviceEndpoints);
        
        // Policy Rules Endpoints
        Map<String, String> ruleEndpoints = new HashMap<>();
        ruleEndpoints.put("create_rule", "POST /api/rules - Create policy rule");
        ruleEndpoints.put("update_rule", "PUT /api/rules/{id} - Update rule");
        ruleEndpoints.put("get_active_rules", "GET /api/rules/active - List active rules");
        ruleEndpoints.put("list_all_rules", "GET /api/rules - List all rules");
        endpoints.put("policy_rules", ruleEndpoints);
        
        // Violation Records Endpoints
        Map<String, String> violationEndpoints = new HashMap<>();
        violationEndpoints.put("log_violation", "POST /api/violations - Log violation");
        violationEndpoints.put("get_user_violations", "GET /api/violations/user/{userId} - Get violations by user");
        violationEndpoints.put("mark_resolved", "PUT /api/violations/{id}/resolve - Mark as resolved");
        violationEndpoints.put("get_unresolved", "GET /api/violations/unresolved - List unresolved violations");
        violationEndpoints.put("list_all_violations", "GET /api/violations - List all violations");
        endpoints.put("violation_records", violationEndpoints);
        
        info.put("endpoints", endpoints);
        
        // Required Headers
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer {jwt_token} - Required for protected endpoints");
        headers.put("Content-Type", "application/json - For request bodies");
        info.put("headers", headers);
        
        // Roles and Permissions
        Map<String, String> roles = new HashMap<>();
        roles.put("ADMIN", "Full access to all endpoints");
        roles.put("AUDITOR", "Read access to all data");
        roles.put("USER", "Basic access, can register devices");
        info.put("roles", roles);
        
        return ResponseEntity.ok(info);
    }
    
    // ==================== AUTHENTICATION ENDPOINTS ====================
    
    @Operation(summary = "Register a new user", description = "Create a new user account with employee ID, username, email, and password")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
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
            response.put("role", createdUser.getRole());
            response.put("status", createdUser.getStatus());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Registration failed");
            error.put("details", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @Operation(summary = "Login user", description = "Authenticate user and return JWT token")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
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
            error.put("details", "Authentication failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
}