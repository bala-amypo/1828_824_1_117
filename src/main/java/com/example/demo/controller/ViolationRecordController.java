package com.example.demo.controller;

import com.example.demo.entity.ViolationRecord;
import com.example.demo.service.ViolationRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Violation Records", description = "Violation record management")
@RestController
@RequestMapping("/api/violations")
public class ViolationRecordController {
    
    private final ViolationRecordService violationRecordService;
    
    public ViolationRecordController(ViolationRecordService violationRecordService) {
        this.violationRecordService = violationRecordService;
    }
    
    @Operation(summary = "Log violation")
    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<ViolationRecord> logViolation(@RequestBody ViolationRecord violation) {
        ViolationRecord loggedViolation = violationRecordService.logViolation(violation);
        return ResponseEntity.status(HttpStatus.CREATED).body(loggedViolation);
    }
    
    @Operation(summary = "Get violations by user")
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<ViolationRecord>> getViolationsByUser(@PathVariable Long userId) {
        List<ViolationRecord> violations = violationRecordService.getViolationsByUser(userId);
        return ResponseEntity.ok(violations);
    }
    
    @Operation(summary = "Mark violation as resolved")
    @PutMapping("/{id}/resolve")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ViolationRecord> markResolved(@PathVariable Long id) {
        ViolationRecord resolvedViolation = violationRecordService.markResolved(id);
        return ResponseEntity.ok(resolvedViolation);
    }
    
    @Operation(summary = "List unresolved violations")
    @GetMapping("/unresolved")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<ViolationRecord>> getUnresolvedViolations() {
        List<ViolationRecord> unresolved = violationRecordService.getUnresolvedViolations();
        return ResponseEntity.ok(unresolved);
    }
    
    @Operation(summary = "List all violations")
    @GetMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<ViolationRecord>> getAllViolations() {
        List<ViolationRecord> allViolations = violationRecordService.getAllViolations();
        return ResponseEntity.ok(allViolations);
    }
}