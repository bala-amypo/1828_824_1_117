package com.example.demo.service.impl;

import com.example.demo.entity.PolicyRule;
import com.example.demo.repository.PolicyRuleRepository;
import com.example.demo.service.PolicyRuleService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PolicyRuleServiceImpl implements PolicyRuleService {

    private final PolicyRuleRepository ruleRepo;

    public PolicyRuleServiceImpl(PolicyRuleRepository ruleRepo) {
        this.ruleRepo = ruleRepo;
    }

    @Override
    public PolicyRule createRule(PolicyRule rule) {
        return ruleRepo.save(rule);
    }

    @Override
    public List<PolicyRule> getActiveRules() {
        return ruleRepo.findByActiveTrue();
    }

    @Override
    public List<PolicyRule> getAllRules() {
        return ruleRepo.findAll();
    }

    // FIX: Added the missing method required by the interface
    @Override
    public Optional<PolicyRule> getRuleByCode(String ruleCode) {
        return ruleRepo.findAll().stream()
                .filter(r -> r.getRuleCode().equals(ruleCode))
                .findFirst();
    }

    @Override
    public PolicyRule updateRule(Long id, PolicyRule updated) {
        return ruleRepo.findById(id).map(rule -> {
            rule.setRuleCode(updated.getRuleCode());
            rule.setDescription(updated.getDescription());
            rule.setSeverity(updated.getSeverity());
            rule.setConditionsJson(updated.getConditionsJson());
            rule.setActive(updated.getActive());
            return ruleRepo.save(rule);
        }).orElse(null);
    }
}