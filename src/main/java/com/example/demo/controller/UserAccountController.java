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

    
    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    
    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<UserAccount> createNewUser(@RequestBody UserAccount user) {
        UserAccount createdUser = userAccountService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

   
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<UserAccount> getUserById(@PathVariable Long id) {
        UserAccount user = userAccountService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<UserAccount> updateAccountStatus(@PathVariable Long id, @RequestParam String status) {
        UserAccount updatedUser = userAccountService.updateUserStatus(id, status);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<UserAccount>> getAllUsers() {
        List<UserAccount> users = userAccountService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}