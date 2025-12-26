package com.example.demo.util;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import java.time.LocalDateTime;

public class RuleEvaluationUtil {
    private final PolicyRuleRepository ruleRepo;
    private final ViolationRecordRepository violationRepo;

    // Exact constructor required by Step 0.3
    public RuleEvaluationUtil(PolicyRuleRepository ruleRepo, ViolationRecordRepository violationRepo) {
        this.ruleRepo = ruleRepo;
        this.violationRepo = violationRepo;
    }

    public void evaluateLoginEvent(LoginEvent event) {
        ruleRepo.findByActiveTrue().forEach(rule -> {
            // Priority 19: Trigger violation if status matches rule condition
            if (rule.getConditionsJson() != null && rule.getConditionsJson().contains(event.getLoginStatus())) {
                ViolationRecord v = new ViolationRecord();
                v.setUserId(event.getUserId());
                v.setEventId(event.getId());
                v.setSeverity(rule.getSeverity()); // Inheritance requirement
                v.setDetails("Policy Violation: " + rule.getDescription());
                v.setDetectedAt(LocalDateTime.now());
                v.setResolved(false);
                violationRepo.save(v);
            }
        });
    }
}