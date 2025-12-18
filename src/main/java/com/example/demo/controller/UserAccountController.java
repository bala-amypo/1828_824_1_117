package com.example.demo.controller;

import com.example.demo.entity.UserAccount;
import com.example.demo.service.UserAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User Accounts", description = "Endpoints for managing user accounts")
@RestController
@RequestMapping("/api/users")
public class UserAccountController {

    private final UserAccountService userAccountService;

    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @Operation(summary = "Create a new user account")
    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<UserAccount> createUser(@RequestBody UserAccount user) {
        UserAccount createdUser = userAccountService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<UserAccount> getUserById(@PathVariable Long id) {
        return userAccountService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get user by username")
    @GetMapping("/username/{username}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<UserAccount> getUserByUsername(@PathVariable String username) {
        UserAccount user = userAccountService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Get all users with pagination")
    @GetMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<Page<UserAccount>> getAllUsers(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<UserAccount> users = userAccountService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get all users (without pagination)")
    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<UserAccount>> getAllUsersList() {
        List<UserAccount> users = userAccountService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Update user account")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<UserAccount> updateUser(@PathVariable Long id, @RequestBody UserAccount user) {
        UserAccount updatedUser = userAccountService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Update user status")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<UserAccount> updateUserStatus(@PathVariable Long id, @RequestParam String status) {
        UserAccount updatedUser = userAccountService.updateUserStatus(id, status);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Delete user account")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userAccountService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search users by keyword")
    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<UserAccount>> searchUsers(@RequestParam String keyword) {
        List<UserAccount> users = userAccountService.searchUsers(keyword);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get users by role")
    @GetMapping("/role/{role}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<UserAccount>> getUsersByRole(@PathVariable String role) {
        List<UserAccount> users = userAccountService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get active users")
    @GetMapping("/active")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<UserAccount>> getActiveUsers() {
        List<UserAccount> users = userAccountService.getActiveUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get inactive users")
    @GetMapping("/inactive")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<UserAccount>> getInactiveUsers() {
        List<UserAccount> users = userAccountService.getInactiveUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get total user count")
    @GetMapping("/count")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<Long> countUsers() {
        Long count = userAccountService.countUsers();
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Get active user count")
    @GetMapping("/count/active")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<Long> countActiveUsers() {
        Long count = userAccountService.countActiveUsers();
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Check if username exists")
    @GetMapping("/exists/username/{username}")
    public ResponseEntity<Boolean> checkUsernameExists(@PathVariable String username) {
        boolean exists = userAccountService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }

    @Operation(summary = "Check if email exists")
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        boolean exists = userAccountService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
}