package com.example.demo.controller;

import com.example.demo.entity.ViolationRecord;
import com.example.demo.service.ViolationRecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Violation Records", description = "Endpoints for managing policy violation records")
@RestController
@RequestMapping("/api/violations")
public class ViolationRecordController {

    private final ViolationRecordService violationRecordService;

    // CONSTRUCTOR INJECTION
    public ViolationRecordController(ViolationRecordService violationRecordService) {
        this.violationRecordService = violationRecordService;
    }

    // POST /api/violations/ - Log violation (Internal system call)
    @PostMapping("/")
    public ResponseEntity<ViolationRecord> logViolation(@RequestBody ViolationRecord violation) {
        ViolationRecord loggedViolation = violationRecordService.logViolation(violation);
        return new ResponseEntity<>(loggedViolation, HttpStatus.CREATED);
    }

    // GET /api/violations/user/{userId} - Get violations by user
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<ViolationRecord>> getViolationsByUser(@PathVariable Long userId) {
        List<ViolationRecord> violations = violationRecordService.getViolationsByUser(userId);
        return ResponseEntity.ok(violations);
    }

    // PUT /api/violations/{id}/resolve - Mark violation as resolved (Role: ADMIN)
    @PutMapping("/{id}/resolve")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ViolationRecord> markResolved(@PathVariable Long id) {
        ViolationRecord resolvedViolation = violationRecordService.markResolved(id);
        return ResponseEntity.ok(resolvedViolation);
    }

    // GET /api/violations/unresolved - List unresolved violations
    @GetMapping("/unresolved")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<ViolationRecord>> getUnresolvedViolations() {
        List<ViolationRecord> unresolved = violationRecordService.getUnresolvedViolations();
        return ResponseEntity.ok(unresolved);
    }

    // GET /api/violations/ - List all violations
    @GetMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<ViolationRecord>> getAllViolations() {
        List<ViolationRecord> allViolations = violationRecordService.getAllViolations();
        return ResponseEntity.ok(allViolations);
    }
}