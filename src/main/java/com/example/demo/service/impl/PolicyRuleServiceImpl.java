package com.example.demo.service.impl;

import com.example.demo.entity.PolicyRule;
import com.example.demo.repository.PolicyRuleRepository;
import com.example.demo.service.PolicyRuleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PolicyRuleServiceImpl implements PolicyRuleService {

    private final PolicyRuleRepository policyRuleRepository;

    public PolicyRuleServiceImpl(PolicyRuleRepository policyRuleRepository) {
        this.policyRuleRepository = policyRuleRepository;
    }

    @Override
    public PolicyRule createRule(PolicyRule rule) {
        // Optional: Add validation for duplicate ruleCode
        if (rule.getActive() == null) {
            rule.setActive(true);
        }
        return policyRuleRepository.save(rule);
    }

    @Override
    @Transactional
    public PolicyRule updateRule(Long id, PolicyRule ruleDetails) {
        return policyRuleRepository.findById(id)
                .map(existingRule -> {
                    // Update only the fields that should be editable
                    if (ruleDetails.getRuleCode() != null) {
                        existingRule.setRuleCode(ruleDetails.getRuleCode());
                    }
                    if (ruleDetails.getDescription() != null) {
                        existingRule.setDescription(ruleDetails.getDescription());
                    }
                    if (ruleDetails.getSeverity() != null) {
                        existingRule.setSeverity(ruleDetails.getSeverity());
                    }
                    if (ruleDetails.getConditionsJson() != null) {
                        existingRule.setConditionsJson(ruleDetails.getConditionsJson());
                    }
                    if (ruleDetails.getActive() != null) {
                        existingRule.setActive(ruleDetails.getActive());
                    }
                    return policyRuleRepository.save(existingRule);
                })
                .orElseThrow(() -> new RuntimeException("Rule not found with id: " + id));
    }

    @Override
    public Optional<PolicyRule> getRuleById(Long id) {
        return policyRuleRepository.findById(id);
    }

    @Override
    public List<PolicyRule> getAllRules() {
        return policyRuleRepository.findAll();
    }

    @Override
    public List<PolicyRule> getActiveRules() {
        return policyRuleRepository.findByActiveTrue();
    }

    @Override
    @Transactional
    public void deleteRule(Long id) {
        policyRuleRepository.deleteById(id);
    }
}