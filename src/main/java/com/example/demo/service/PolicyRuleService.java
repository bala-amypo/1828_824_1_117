package com.example.demo.service;

import com.example.demo.entity.PolicyRule;
import java.util.List;
import java.util.Optional;

public interface PolicyRuleService {
    PolicyRule createRule(PolicyRule rule);
    PolicyRule updateRule(Long id, PolicyRule rule);
    Optional<PolicyRule> getRuleById(Long id);
    List<PolicyRule> getAllRules();
    List<PolicyRule> getActiveRules();
    void deleteRule(Long id);
}