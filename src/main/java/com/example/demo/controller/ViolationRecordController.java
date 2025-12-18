package com.example.demo.controller;

import com.example.demo.entity.ViolationRecord;
import com.example.demo.service.ViolationRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Violation Records", description = "Endpoints for managing policy violation records")
@RestController
@RequestMapping("/api/violations")
public class ViolationRecordController {

    private final ViolationRecordService violationRecordService;

    public ViolationRecordController(ViolationRecordService violationRecordService) {
        this.violationRecordService = violationRecordService;
    }

    @Operation(summary = "Log a new policy violation")
    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<ViolationRecord> logViolation(@RequestBody ViolationRecord violation) {
        ViolationRecord loggedViolation = violationRecordService.logViolation(violation);
        return new ResponseEntity<>(loggedViolation, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all violations (with pagination)")
    @GetMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<Page<ViolationRecord>> getAllViolations(
            @PageableDefault(size = 20, sort = "violationTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ViolationRecord> allViolations = violationRecordService.getAllViolations(pageable);
        return ResponseEntity.ok(allViolations);
    }

    @Operation(summary = "Get all violations (without pagination)")
    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<ViolationRecord>> getAllViolationsList() {
        List<ViolationRecord> allViolations = violationRecordService.getAllViolations();
        return ResponseEntity.ok(allViolations);
    }

    @Operation(summary = "Get a specific violation by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<ViolationRecord> getViolationById(@PathVariable Long id) {
        ViolationRecord violation = violationRecordService.getViolationById(id);
        return ResponseEntity.ok(violation);
    }

    @Operation(summary = "Get violations for a specific user by user ID")
    @GetMapping("/user/id/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<ViolationRecord>> getViolationsByUserId(@PathVariable Long userId) {
        List<ViolationRecord> violations = violationRecordService.getViolationsByUser(userId);
        return ResponseEntity.ok(violations);
    }

    @Operation(summary = "Get violations for a specific user by username")
    @GetMapping("/user/username/{username}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<ViolationRecord>> getViolationsByUsername(@PathVariable String username) {
        List<ViolationRecord> violations = violationRecordService.getViolationsByUser(username);
        return ResponseEntity.ok(violations);
    }

    @Operation(summary = "Mark a violation as resolved")
    @PutMapping("/{id}/resolve")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ViolationRecord> markResolved(@PathVariable Long id) {
        ViolationRecord resolvedViolation = violationRecordService.markResolved(id);
        return ResponseEntity.ok(resolvedViolation);
    }

    @Operation(summary = "Mark a violation as unresolved")
    @PutMapping("/{id}/unresolve")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ViolationRecord> markUnresolved(@PathVariable Long id) {
        ViolationRecord unresolvedViolation = violationRecordService.markUnresolved(id);
        return ResponseEntity.ok(unresolvedViolation);
    }

    @Operation(summary = "Get all unresolved violations")
    @GetMapping("/unresolved")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<ViolationRecord>> getUnresolvedViolations() {
        List<ViolationRecord> unresolved = violationRecordService.getUnresolvedViolations();
        return ResponseEntity.ok(unresolved);
    }

    @Operation(summary = "Get all resolved violations")
    @GetMapping("/resolved")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<ViolationRecord>> getResolvedViolations() {
        List<ViolationRecord> resolved = violationRecordService.getResolvedViolations();
        return ResponseEntity.ok(resolved);
    }

    @Operation(summary = "Get violations by severity")
    @GetMapping("/severity/{severity}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<ViolationRecord>> getViolationsBySeverity(@PathVariable String severity) {
        List<ViolationRecord> violations = violationRecordService.getViolationsBySeverity(severity);
        return ResponseEntity.ok(violations);
    }

    @Operation(summary = "Get violations by date range")
    @GetMapping("/date-range")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<ViolationRecord>> getViolationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<ViolationRecord> violations = violationRecordService.getViolationsByDateRange(startDate, endDate);
        return ResponseEntity.ok(violations);
    }

    @Operation(summary = "Get count of unresolved violations")
    @GetMapping("/count/unresolved")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<Long> countUnresolvedViolations() {
        Long count = violationRecordService.countUnresolvedViolations();
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Get violation count for a user")
    @GetMapping("/count/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<Long> countViolationsByUser(@PathVariable Long userId) {
        Long count = violationRecordService.countViolationsByUser(userId);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Delete a violation record")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Void> deleteViolation(@PathVariable Long id) {
        violationRecordService.deleteViolation(id);
        return ResponseEntity.noContent().build();
    }
}