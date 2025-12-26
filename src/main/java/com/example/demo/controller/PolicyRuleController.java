package com.example.demo.controller;
import com.example.demo.entity.PolicyRule;
import com.example.demo.service.PolicyRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rules")
public class PolicyRuleController {
    private final PolicyRuleService ruleService;
    public PolicyRuleController(PolicyRuleService ruleService) { this.ruleService = ruleService; }

    @GetMapping
    public ResponseEntity<List<PolicyRule>> all() { // Matches priority 27
        return ResponseEntity.ok(ruleService.getAllRules());
    }
}