package com.example.demo.controller;

import com.example.demo.dto.JwtResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.UserAccount;
import com.example.demo.service.UserAccountService;
import com.example.demo.util.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "User registration and login endpoints")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserAccountService userAccountService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    // CONSTRUCTOR INJECTION (Required by Technical Constraint 2)
    public AuthController(
            UserAccountService userAccountService, 
            AuthenticationManager authenticationManager, 
            JwtUtil jwtUtil) {
        this.userAccountService = userAccountService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * POST /auth/register - Create a new UserAccount
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest) {
        // 1. Map DTO to Entity (Simplified - you might need a dedicated mapper)
        UserAccount newUser = new UserAccount();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(registerRequest.getPassword()); // Service will encode this
        newUser.setEmail(registerRequest.getEmail());
        newUser.setEmployeeId(registerRequest.getEmployeeId());
        // Default role is USER, or logic to assign ADMIN/AUDITOR based on requirement
        newUser.setRole(registerRequest.getRole() != null ? registerRequest.getRole() : "USER");
        
        // 2. Save user via service
        userAccountService.createUser(newUser);

        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }

    /**
     * POST /auth/login - Authenticate user and return JWT
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        
        // 1. Authenticate using Spring Security's AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // 2. Set authentication in SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Generate JWT token
        String jwt = jwtUtil.generateToken(authentication);

        // 4. Return the token in the response DTO
        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}