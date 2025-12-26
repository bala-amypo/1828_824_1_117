package com.example.demo.util;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import java.time.LocalDateTime;

public class RuleEvaluationUtil {
    private final PolicyRuleRepository ruleRepo;
    private final ViolationRecordRepository violationRepo;

    // STEP 0.3 - Mandatory Constructor order
    public RuleEvaluationUtil(PolicyRuleRepository ruleRepo, ViolationRecordRepository violationRepo) {
        this.ruleRepo = ruleRepo;
        this.violationRepo = violationRepo;
    }

    public void evaluateLoginEvent(LoginEvent event) {
        ruleRepo.findByActiveTrue().forEach(rule -> {
            if (rule.getConditionsJson() != null && rule.getConditionsJson().contains(event.getLoginStatus())) {
                ViolationRecord v = new ViolationRecord();
                v.setUserId(event.getUserId());
                v.setEventId(event.getId());
                v.setSeverity(rule.getSeverity()); // Rule: Severity inherited from PolicyRule
                v.setDetectedAt(LocalDateTime.now());
                v.setResolved(false); // Rule: defaults to false
                violationRepo.save(v);
            }
        });
    }
}