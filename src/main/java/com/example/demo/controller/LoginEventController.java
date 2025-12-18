package com.example.demo.controller;

import com.example.demo.entity.LoginEvent;
import com.example.demo.service.LoginEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Login Events", description = "Endpoints for logging and retrieving user login events")
@RestController
@RequestMapping("/api/logins")
public class LoginEventController {

    private final LoginEventService loginEventService;

    public LoginEventController(LoginEventService loginEventService) {
        this.loginEventService = loginEventService;
    }

    @Operation(summary = "Record a new login event")
    @PostMapping("/record")
    public ResponseEntity<LoginEvent> recordLogin(@RequestBody LoginEvent event) {
        LoginEvent loggedEvent = loginEventService.recordLogin(event);
        return new ResponseEntity<>(loggedEvent, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all login events")
    @GetMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<LoginEvent>> getAllLoginEvents() {
        List<LoginEvent> allEvents = loginEventService.getAllEvents();
        return ResponseEntity.ok(allEvents);
    }

    @Operation(summary = "Get login events for a specific user by user ID")
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<LoginEvent>> getEventsForUserById(@PathVariable Long userId) {
        List<LoginEvent> events = loginEventService.getEventsByUser(userId);
        return ResponseEntity.ok(events);
    }

    @Operation(summary = "Get login events for a specific user by username")
    @GetMapping("/username/{username}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR') or @securityService.isUserSelf(#username)")
    public ResponseEntity<List<LoginEvent>> getEventsForUser(@PathVariable String username) {
        List<LoginEvent> events = loginEventService.getEventsByUser(username);
        return ResponseEntity.ok(events);
    }

    @Operation(summary = "Get suspicious login events for a user by user ID")
    @GetMapping("/suspicious/id/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<LoginEvent>> getSuspiciousLoginsById(@PathVariable Long userId) {
        List<LoginEvent> suspiciousEvents = loginEventService.getSuspiciousLogins(userId);
        return ResponseEntity.ok(suspiciousEvents);
    }

    @Operation(summary = "Get suspicious login events for a user by username")
    @GetMapping("/suspicious/username/{username}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<LoginEvent>> getSuspiciousLogins(@PathVariable String username) {
        List<LoginEvent> suspiciousEvents = loginEventService.getSuspiciousLogins(username);
        return ResponseEntity.ok(suspiciousEvents);
    }

    @Operation(summary = "Get a specific login event by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<LoginEvent> getEventById(@PathVariable Long id) {
        LoginEvent event = loginEventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    @Operation(summary = "Delete a login event")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        loginEventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}