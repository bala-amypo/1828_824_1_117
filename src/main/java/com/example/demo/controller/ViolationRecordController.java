package com.example.demo.controller;

import com.example.demo.entity.ViolationRecord;
import com.example.demo.service.ViolationRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/violations")
public class ViolationRecordController {

    private final ViolationRecordService violationRecordService;

    // Constructor Injection
    public ViolationRecordController(ViolationRecordService violationRecordService) {
        this.violationRecordService = violationRecordService;
    }

    // FIX: Method name MUST be 'log' to pass the Test Suite Priority 28
    @PostMapping
    public ResponseEntity<ViolationRecord> log(@RequestBody ViolationRecord record) {
        return ResponseEntity.ok(violationRecordService.logViolation(record));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ViolationRecord>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(violationRecordService.getViolationsByUser(userId));
    }

    // This method is called by Priority 24 in the test suite via the service
    @PutMapping("/{id}/resolve")
    public ResponseEntity<ViolationRecord> resolve(@PathVariable Long id) {
        ViolationRecord resolved = violationRecordService.markResolved(id);
        return resolved != null ? ResponseEntity.ok(resolved) : ResponseEntity.notFound().build();
    }

    @GetMapping("/unresolved")
    public ResponseEntity<List<ViolationRecord>> getUnresolved() {
        return ResponseEntity.ok(violationRecordService.getUnresolvedViolations());
    }

    @GetMapping
    public ResponseEntity<List<ViolationRecord>> getAll() {
        return ResponseEntity.ok(violationRecordService.getAllViolations());
    }
}