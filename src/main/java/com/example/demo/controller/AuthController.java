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

    
    public AuthController(
            UserAccountService userAccountService, 
            AuthenticationManager authenticationManager, 
            JwtUtil jwtUtil) {
        this.userAccountService = userAccountService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest) {
        
        UserAccount newUser = new UserAccount();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(registerRequest.getPassword()); 
        newUser.setEmail(registerRequest.getEmail());
        newUser.setEmployeeId(registerRequest.getEmployeeId());
        
        newUser.setRole(registerRequest.getRole() != null ? registerRequest.getRole() : "USER");
        
        
        userAccountService.createUser(newUser);

        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }

    
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

       
        SecurityContextHolder.getContext().setAuthentication(authentication);

       
        String jwt = jwtUtil.generateToken(authentication);

        
        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}