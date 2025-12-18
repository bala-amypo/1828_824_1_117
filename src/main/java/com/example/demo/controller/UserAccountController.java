package com.example.demo.controller;

import com.example.demo.entity.UserAccount;
import com.example.demo.service.UserAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User Management", description = "User account management")
@RestController
@RequestMapping("/api/users")
public class UserAccountController {
    
    private final UserAccountService userAccountService;
    
    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }
    
    @Operation(summary = "Create new user")
    @PostMapping("/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserAccount> createUser(@RequestBody UserAccount user) {
        UserAccount createdUser = userAccountService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    
    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<UserAccount> getUserById(@PathVariable Long id) {
        UserAccount user = userAccountService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    @Operation(summary = "Update account status")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserAccount> updateUserStatus(@PathVariable Long id, 
                                                       @RequestParam String status) {
        UserAccount updatedUser = userAccountService.updateUserStatus(id, status);
        return ResponseEntity.ok(updatedUser);
    }
    
    @Operation(summary = "List all users")
    @GetMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<UserAccount>> getAllUsers() {
        List<UserAccount> users = userAccountService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}