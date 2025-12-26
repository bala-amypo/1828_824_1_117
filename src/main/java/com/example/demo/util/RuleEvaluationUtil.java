package com.example.demo.util;

import com.example.demo.entity.LoginEvent;
import com.example.demo.entity.ViolationRecord;
import com.example.demo.repository.PolicyRuleRepository;
import com.example.demo.repository.ViolationRecordRepository;
import java.time.LocalDateTime;

/**
 * Utility for evaluating policy rules against login events.
 * Registered as a @Bean in AppConfig.
 */
public class RuleEvaluationUtil {

    private final PolicyRuleRepository ruleRepo;
    private final ViolationRecordRepository violationRepo;

    public RuleEvaluationUtil(PolicyRuleRepository ruleRepo, ViolationRecordRepository violationRepo) {
        this.ruleRepo = ruleRepo;
        this.violationRepo = violationRepo;
    }

    /**
     * Evaluates a login event against all active policy rules.
     * If a rule condition is met (e.g., status is FAILED), a violation is logged.
     */
    public void evaluateLoginEvent(LoginEvent event) {
        // Fetch only rules that are currently active
        ruleRepo.findByActiveTrue().forEach(rule -> {
            
            // Basic condition check: Does the rule condition match the login status?
            // Priority 19 check: Tests if a violation is triggered when status is "FAILED"
            if (rule.getConditionsJson() != null && 
                rule.getConditionsJson().equalsIgnoreCase(event.getLoginStatus())) {
                
                ViolationRecord violation = new ViolationRecord();
                violation.setUserId(event.getUserId());
                violation.setEventId(event.getId());
                violation.setSeverity(rule.getSeverity());
                violation.setDetails("Policy Violation: " + rule.getRuleCode());
                violation.setDetectedAt(LocalDateTime.now());
                violation.setResolved(false);
                
                // Save the violation record to the database
                violationRepo.save(violation);
            }
        });
    }
}