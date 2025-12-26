package com.example.demo.controller;

import com.example.demo.entity.PolicyRule;
import com.example.demo.service.PolicyRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
public class PolicyRuleController {

    private final PolicyRuleService policyRuleService;

    public PolicyRuleController(PolicyRuleService policyRuleService) {
        this.policyRuleService = policyRuleService;
    }

    @PostMapping
    public ResponseEntity<PolicyRule> create(@RequestBody PolicyRule rule) {
        return ResponseEntity.ok(policyRuleService.createRule(rule));
    }

    // FIX: Method name must be 'all' to satisfy priority 27 in the test suite
    @GetMapping
    public ResponseEntity<List<PolicyRule>> all() {
        return ResponseEntity.ok(policyRuleService.getAllRules());
    }

    @GetMapping("/active")
    public ResponseEntity<List<PolicyRule>> getActiveRules() {
        return ResponseEntity.ok(policyRuleService.getActiveRules());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PolicyRule> update(@PathVariable Long id, @RequestBody PolicyRule rule) {
        PolicyRule updated = policyRuleService.updateRule(id, rule);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
}