package com.example.demo.service.impl;

import com.example.demo.entity.PolicyRule;
import com.example.demo.repository.PolicyRuleRepository;
import com.example.demo.service.PolicyRuleService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PolicyRuleServiceImpl implements PolicyRuleService {

    private final PolicyRuleRepository policyRuleRepository;

    public PolicyRuleServiceImpl(PolicyRuleRepository policyRuleRepository) {
        this.policyRuleRepository = policyRuleRepository;
    }

    @Override
    public PolicyRule createRule(PolicyRule rule) {
        return policyRuleRepository.save(rule);
    }

    @Override
    public List<PolicyRule> getAllRules() {
        return policyRuleRepository.findAll();
    }

    @Override
    public List<PolicyRule> getActiveRules() {
        return policyRuleRepository.findByActiveTrue();
    }
}