package com.example.demo.util;

import com.example.demo.entity.LoginEvent;
import com.example.demo.entity.PolicyRule;
import com.example.demo.entity.ViolationRecord;
import com.example.demo.repository.PolicyRuleRepository;
import com.example.demo.repository.ViolationRecordRepository;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class RuleEvaluationUtil {

    private final PolicyRuleRepository policyRuleRepository;
    private final ViolationRecordRepository violationRecordRepository;

    // CONSTRUCTOR INJECTION (Constraint 3)
    public RuleEvaluationUtil(PolicyRuleRepository policyRuleRepository, 
                                ViolationRecordRepository violationRecordRepository) {
        this.policyRuleRepository = policyRuleRepository;
        this.violationRecordRepository = violationRecordRepository;
    }

    /**
     * Core logic to detect violations based on login activity
     */
    public void evaluateLoginEvent(LoginEvent event) {
        // 1. Fetch all active rules
        List<PolicyRule> activeRules = policyRuleRepository.findByActiveTrue();

        for (PolicyRule rule : activeRules) {
            boolean isViolation = false;
            String detailMessage = "";

            // 2. Simple logic example: Detect failed logins (Brute Force Rule)
            if (rule.getRuleCode().equals("BRUTE_FORCE") && "FAILED".equals(event.getLoginStatus())) {
                isViolation = true;
                detailMessage = "Failed login attempt detected from IP: " + event.getIpAddress();
            }

            // 3. Simple logic example: Detect untrusted devices
            if (rule.getRuleCode().equals("UNTRUSTED_DEVICE") && event.getDeviceId().startsWith("UNKNOWN")) {
                isViolation = true;
                detailMessage = "Login attempt from an unrecognized device hardware ID.";
            }

            // 4. If a violation is detected, log it to the database
            if (isViolation) {
                ViolationRecord record = new ViolationRecord();
                record.setUserId(event.getUserId());
                record.setPolicyRuleId(rule.getId());
                record.setEventId(event.getId());
                record.setViolationType(rule.getRuleCode());
                record.setSeverity(rule.getSeverity());
                record.setDetails(detailMessage);
                record.setResolved(false);

                violationRecordRepository.save(record);
            }
        }
    }
}