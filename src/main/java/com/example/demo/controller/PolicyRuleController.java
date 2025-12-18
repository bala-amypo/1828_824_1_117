package com.example.demo.controller;

import com.example.demo.entity.PolicyRule;
import com.example.demo.service.PolicyRuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Policy Rules", description = "Endpoints for managing violation detection rules")
@RestController
@RequestMapping("/api/rules")
public class PolicyRuleController {

    private final PolicyRuleService policyRuleService;

    
    public PolicyRuleController(PolicyRuleService policyRuleService) {
        this.policyRuleService = policyRuleService;
    }

    
    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<PolicyRule> createRule(@RequestBody PolicyRule rule) {
        PolicyRule createdRule = policyRuleService.createRule(rule);
        return new ResponseEntity<>(createdRule, HttpStatus.CREATED);
    }

    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<PolicyRule> updateRule(@PathVariable Long id, @RequestBody PolicyRule rule) {
        PolicyRule updatedRule = policyRuleService.updateRule(id, rule);
        return ResponseEntity.ok(updatedRule);
    }
    
    
    @GetMapping("/active")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<PolicyRule>> getActiveRules() {
        List<PolicyRule> activeRules = policyRuleService.getActiveRules();
        return ResponseEntity.ok(activeRules);
    }

    
    @GetMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<PolicyRule>> getAllRules() {
        List<PolicyRule> allRules = policyRuleService.getAllRules();
        return ResponseEntity.ok(allRules);
    }
}