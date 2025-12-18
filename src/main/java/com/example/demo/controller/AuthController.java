package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.JwtResponse;
import com.example.demo.entity.UserAccount;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Tag(name = "Authentication", description = "User authentication and registration endpoints")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager,
                         UserAccountRepository userAccountRepository,
                         PasswordEncoder passwordEncoder,
                         JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "Authenticate user")
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtil.generateToken(authentication);

            org.springframework.security.core.userdetails.UserDetails userDetails = 
                (org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal();
            
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // Get user from database for additional info
            Optional<UserAccount> userOpt = userAccountRepository.findByUsername(loginRequest.getUsername());
            
            JwtResponse response = new JwtResponse();
            response.setToken(jwt);
            response.setUsername(userDetails.getUsername());
            response.setRoles(roles);
            
            if (userOpt.isPresent()) {
                response.setId(userOpt.get().getId());
                response.setEmail(userOpt.get().getEmail());
                response.setEmployeeId(userOpt.get().getEmployeeId());
            }

            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"error\": \"Invalid username or password\"}");
        }
    }

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            // Check if username exists
            if (userAccountRepository.existsByUsername(registerRequest.getUsername())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"error\": \"Username is already taken!\"}");
            }

            // Check if email exists
            if (userAccountRepository.existsByEmail(registerRequest.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"error\": \"Email is already in use!\"}");
            }

            // Check if employeeId exists
            if (registerRequest.getEmployeeId() != null && 
                userAccountRepository.existsByEmployeeId(registerRequest.getEmployeeId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"error\": \"Employee ID already exists!\"}");
            }

            // Create new user account
            UserAccount user = new UserAccount();
            user.setUsername(registerRequest.getUsername());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setEmail(registerRequest.getEmail());
            user.setEmployeeId(registerRequest.getEmployeeId());
            user.setRole(registerRequest.getRole() != null ? registerRequest.getRole() : "BASE");

            UserAccount savedUser = userAccountRepository.save(user);

            // Generate JWT token for immediate login after registration
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    registerRequest.getUsername(),
                    registerRequest.getPassword()
                )
            );

            String jwt = jwtUtil.generateToken(authentication);
            org.springframework.security.core.userdetails.UserDetails userDetails = 
                (org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal();
            
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            JwtResponse response = new JwtResponse();
            response.setToken(jwt);
            response.setId(savedUser.getId());
            response.setUsername(savedUser.getUsername());
            response.setEmail(savedUser.getEmail());
            response.setEmployeeId(savedUser.getEmployeeId());
            response.setRoles(roles);
            response.setMessage("User registered successfully!");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Registration failed: " + e.getMessage() + "\"}");
        }
    }

    @Operation(summary = "Check if user exists")
    @GetMapping("/check-username/{username}")
    public ResponseEntity<?> checkUsername(@PathVariable String username) {
        boolean exists = userAccountRepository.existsByUsername(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get current user info")
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() || 
            authentication.getName().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authentication.getName();
        Optional<UserAccount> userOpt = userAccountRepository.findByUsername(username);
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"User not found\"}");
        }

        UserAccount user = userOpt.get();
        return ResponseEntity.ok(user);
    }
}