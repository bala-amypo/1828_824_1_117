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

@Tag(name = "Login Events", description = "Login event management")
@RestController
@RequestMapping("/api/logins")
public class LoginEventController {
    
    private final LoginEventService loginEventService;
    
    public LoginEventController(LoginEventService loginEventService) {
        this.loginEventService = loginEventService;
    }
    
    @Operation(summary = "Record login attempt")
    @PostMapping("/record")
    public ResponseEntity<LoginEvent> recordLogin(@RequestBody LoginEvent event) {
        LoginEvent loggedEvent = loginEventService.recordLogin(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(loggedEvent);
    }
    
    @Operation(summary = "Get login events for user")
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<LoginEvent>> getEventsForUser(@PathVariable Long userId) {
        List<LoginEvent> events = loginEventService.getEventsByUser(userId);
        return ResponseEntity.ok(events);
    }
    
    @Operation(summary = "Get suspicious logins for user")
    @GetMapping("/suspicious/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<LoginEvent>> getSuspiciousLogins(@PathVariable Long userId) {
        List<LoginEvent> suspiciousEvents = loginEventService.getSuspiciousLogins(userId);
        return ResponseEntity.ok(suspiciousEvents);
    }
    
    @Operation(summary = "List all login events")
    @GetMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<LoginEvent>> getAllLoginEvents() {
        List<LoginEvent> allEvents = loginEventService.getAllEvents();
        return ResponseEntity.ok(allEvents);
    }
}