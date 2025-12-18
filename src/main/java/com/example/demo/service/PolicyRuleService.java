package com.example.demo.service;

import com.example.demo.entity.PolicyRule;
import java.util.List;
import java.util.Optional;

public interface PolicyRuleService {
    PolicyRule createRule(PolicyRule rule);
    PolicyRule updateRule(Long id, PolicyRule rule);
    List<PolicyRule> getActiveRules();
    Optional<PolicyRule> getRuleByCode(String ruleCode);
    List<PolicyRule> getAllRules();
}