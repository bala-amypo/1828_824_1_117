package com.example.demo.util;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import java.time.LocalDateTime;

public class RuleEvaluationUtil {
    private final PolicyRuleRepository ruleRepo;
    private final ViolationRecordRepository violationRepo;

    public RuleEvaluationUtil(PolicyRuleRepository ruleRepo, ViolationRecordRepository violationRepo) {
        this.ruleRepo = ruleRepo;
        this.violationRepo = violationRepo;
    }

    public void evaluateLoginEvent(LoginEvent event) {
        ruleRepo.findByActiveTrue().forEach(rule -> {
            // Check if rule conditions contain the event status (e.g., "FAILED")
            if (rule.getConditionsJson() != null && rule.getConditionsJson().contains(event.getLoginStatus())) {
                ViolationRecord v = new ViolationRecord();
                v.setUserId(event.getUserId());
                v.setEventId(event.getId());
                v.setSeverity(rule.getSeverity()); // Priority 37 & 59
                v.setDetails("Policy Violation: " + rule.getRuleCode());
                v.setDetectedAt(LocalDateTime.now());
                v.setResolved(false); // Default for Priority 23/24
                violationRepo.save(v);
            }
        });
    }
}