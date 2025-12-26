package com.example.demo.util;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import java.time.LocalDateTime;

public class RuleEvaluationUtil {
    private final PolicyRuleRepository ruleRepo;
    private final ViolationRecordRepository violationRepo;

    // Constructor order must match Test Class init()
    public RuleEvaluationUtil(PolicyRuleRepository ruleRepo, ViolationRecordRepository violationRepo) {
        this.ruleRepo = ruleRepo;
        this.violationRepo = violationRepo;
    }

    public void evaluateLoginEvent(LoginEvent event) {
        ruleRepo.findByActiveTrue().forEach(rule -> {
            // Priority 19 logic: Match status against conditions
            if (rule.getConditionsJson() != null && rule.getConditionsJson().contains(event.getLoginStatus())) {
                ViolationRecord v = new ViolationRecord();
                v.setUserId(event.getUserId());
                v.setEventId(event.getId());
                v.setSeverity(rule.getSeverity());
                v.setDetails("Policy Violation Triggered");
                v.setDetectedAt(LocalDateTime.now());
                v.setResolved(false);
                violationRepo.save(v);
            }
        });
    }
}