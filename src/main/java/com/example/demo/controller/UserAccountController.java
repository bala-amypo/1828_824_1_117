package com.example.demo.controller;

import com.example.demo.entity.UserAccount;
import com.example.demo.service.UserAccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User Management", description = "Endpoints for managing user accounts")
@RestController
@RequestMapping("/api/users")
public class UserAccountController {

    private final UserAccountService userAccountService;

    // CONSTRUCTOR INJECTION
    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    // POST /api/users - Create new user (Role: ADMIN)
    // Note: Registration is handled in AuthController for general users, 
    // this endpoint can be reserved for admin-created accounts or specific DTOs.
    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<UserAccount> createNewUser(@RequestBody UserAccount user) {
        UserAccount createdUser = userAccountService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    // GET /api/users/{id} - Get user by ID (Role: ADMIN, or self)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<UserAccount> getUserById(@PathVariable Long id) {
        UserAccount user = userAccountService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // PUT /api/users/{id}/status - Update account status (Role: ADMIN)
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<UserAccount> updateAccountStatus(@PathVariable Long id, @RequestParam String status) {
        UserAccount updatedUser = userAccountService.updateUserStatus(id, status);
        return ResponseEntity.ok(updatedUser);
    }

    // GET /api/users - List all users (Role: ADMIN, AUDITOR)
    @GetMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<UserAccount>> getAllUsers() {
        List<UserAccount> users = userAccountService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}