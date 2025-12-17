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

    
    public RuleEvaluationUtil(PolicyRuleRepository policyRuleRepository, 
                                ViolationRecordRepository violationRecordRepository) {
        this.policyRuleRepository = policyRuleRepository;
        this.violationRecordRepository = violationRecordRepository;
    }

   
    public void evaluateLoginEvent(LoginEvent event) {
       
        List<PolicyRule> activeRules = policyRuleRepository.findByActiveTrue();

        for (PolicyRule rule : activeRules) {
            boolean isViolation = false;
            String detailMessage = "";

            
            if (rule.getRuleCode().equals("BRUTE_FORCE") && "FAILED".equals(event.getLoginStatus())) {
                isViolation = true;
                detailMessage = "Failed login attempt detected from IP: " + event.getIpAddress();
            }

            if (rule.getRuleCode().equals("UNTRUSTED_DEVICE") && event.getDeviceId().startsWith("UNKNOWN")) {
                isViolation = true;
                detailMessage = "Login attempt from an unrecognized device hardware ID.";
            }

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