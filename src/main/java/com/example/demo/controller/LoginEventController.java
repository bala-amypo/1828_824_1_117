package com.example.demo.controller;

import com.example.demo.entity.LoginEvent;
import com.example.demo.service.LoginEventService;
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

    // CONSTRUCTOR INJECTION
    public LoginEventController(LoginEventService loginEventService) {
        this.loginEventService = loginEventService;
    }

    // POST /api/logins/record - Log a login attempt (Usually internal or system call)
    @PostMapping("/record")
    public ResponseEntity<LoginEvent> recordLogin(@RequestBody LoginEvent event) {
        LoginEvent loggedEvent = loginEventService.recordLogin(event);
        return new ResponseEntity<>(loggedEvent, HttpStatus.CREATED);
    }

    // GET /api/logins/{userId} - Get login events for a specific user
    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR') or @securityService.isUserSelf(#userId)")
    public ResponseEntity<List<LoginEvent>> getEventsForUser(@PathVariable Long userId) {
        List<LoginEvent> events = loginEventService.getEventsByUser(userId);
        return ResponseEntity.ok(events);
    }

    // GET /api/logins/suspicious/{userId} - Get suspicious login activity for user
    @GetMapping("/suspicious/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<LoginEvent>> getSuspiciousLogins(@PathVariable Long userId) {
        List<LoginEvent> suspiciousEvents = loginEventService.getSuspiciousLogins(userId);
        return ResponseEntity.ok(suspiciousEvents);
    }
    
    // GET /api/logins/ - List all login events
    @GetMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<LoginEvent>> getAllLoginEvents() {
        List<LoginEvent> allEvents = loginEventService.getAllEvents();
        return ResponseEntity.ok(allEvents);
    }
}