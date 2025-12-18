package com.example.demo.service.impl;

import com.example.demo.entity.PolicyRule;
import com.example.demo.repository.PolicyRuleRepository;
import com.example.demo.service.PolicyRuleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PolicyRuleServiceImpl implements PolicyRuleService {
    
    private final PolicyRuleRepository policyRuleRepository;
    
    // EXACT CONSTRUCTOR SIGNATURE AS REQUIRED
    public PolicyRuleServiceImpl(PolicyRuleRepository policyRuleRepository) {
        this.policyRuleRepository = policyRuleRepository;
    }
    
    @Override
    public PolicyRule createRule(PolicyRule rule) {
        // Check unique rule code
        if (policyRuleRepository.findByRuleCode(rule.getRuleCode()).isPresent()) {
            throw new IllegalArgumentException("Rule code already exists");
        }
        
        return policyRuleRepository.save(rule);
    }
    
    @Override
    public PolicyRule updateRule(Long id, PolicyRule rule) {
        PolicyRule existing = policyRuleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy rule not found"));
        
        // Update fields
        existing.setDescription(rule.getDescription());
        existing.setSeverity(rule.getSeverity());
        existing.setConditionsJson(rule.getConditionsJson());
        existing.setActive(rule.getActive());
        
        return policyRuleRepository.save(existing);
    }
    
    @Override
    public List<PolicyRule> getActiveRules() {
        return policyRuleRepository.findByActiveTrue();
    }
    
    @Override
    public Optional<PolicyRule> getRuleByCode(String ruleCode) {
        return policyRuleRepository.findByRuleCode(ruleCode);
    }
    
    @Override
    public List<PolicyRule> getAllRules() {
        return policyRuleRepository.findAll();
    }
}