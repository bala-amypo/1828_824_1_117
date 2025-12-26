package com.example.demo.util;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class RuleEvaluationUtil {
    private final PolicyRuleRepository ruleRepo;
    private final ViolationRecordRepository violationRepo;

    public RuleEvaluationUtil(PolicyRuleRepository ruleRepo, ViolationRecordRepository violationRepo) {
        this.ruleRepo = ruleRepo;
        this.violationRepo = violationRepo;
    }

    public void evaluateLoginEvent(LoginEvent event) {
        ruleRepo.findByActiveTrue().forEach(rule -> {
            if (rule.getConditionsJson() != null && rule.getConditionsJson().contains(event.getLoginStatus())) {
                ViolationRecord v = new ViolationRecord();
                v.setUserId(event.getUserId());
                v.setSeverity(rule.getSeverity());
                v.setDetails("Policy Violation: " + rule.getRuleCode());
                v.setDetectedAt(LocalDateTime.now());
                v.setResolved(false);
                violationRepo.save(v);
            }
        });
    }
}