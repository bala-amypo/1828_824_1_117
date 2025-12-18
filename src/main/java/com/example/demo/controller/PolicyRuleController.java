package com.example.demo.controller;

import com.example.demo.entity.PolicyRule;
import com.example.demo.service.PolicyRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Policy Rules", description = "Policy rule management")
@RestController
@RequestMapping("/api/rules")
public class PolicyRuleController {
    
    private final PolicyRuleService policyRuleService;
    
    public PolicyRuleController(PolicyRuleService policyRuleService) {
        this.policyRuleService = policyRuleService;
    }
    
    @Operation(summary = "Create new rule")
    @PostMapping("/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PolicyRule> createRule(@RequestBody PolicyRule rule) {
        PolicyRule createdRule = policyRuleService.createRule(rule);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRule);
    }
    
    @Operation(summary = "Update rule")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PolicyRule> updateRule(@PathVariable Long id, 
                                                @RequestBody PolicyRule rule) {
        PolicyRule updatedRule = policyRuleService.updateRule(id, rule);
        return ResponseEntity.ok(updatedRule);
    }
    
    @Operation(summary = "List active rules")
    @GetMapping("/active")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<PolicyRule>> getActiveRules() {
        List<PolicyRule> activeRules = policyRuleService.getActiveRules();
        return ResponseEntity.ok(activeRules);
    }
    
    @Operation(summary = "List all rules")
    @GetMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<PolicyRule>> getAllRules() {
        List<PolicyRule> allRules = policyRuleService.getAllRules();
        return ResponseEntity.ok(allRules);
    }
}