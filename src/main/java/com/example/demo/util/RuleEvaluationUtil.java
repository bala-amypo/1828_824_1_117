package com.example.demo.util;

import com.example.demo.entity.LoginEvent;
import com.example.demo.entity.PolicyRule;
import com.example.demo.entity.ViolationRecord;
import com.example.demo.repository.PolicyRuleRepository;
import com.example.demo.repository.ViolationRecordRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class RuleEvaluationUtil {
    
    private final PolicyRuleRepository policyRuleRepository;
    private final ViolationRecordRepository violationRecordRepository;
    
    // EXACT CONSTRUCTOR SIGNATURE AS REQUIRED
    public RuleEvaluationUtil(PolicyRuleRepository policyRuleRepository,
                             ViolationRecordRepository violationRecordRepository) {
        this.policyRuleRepository = policyRuleRepository;
        this.violationRecordRepository = violationRecordRepository;
    }
    
    public void evaluateLoginEvent(LoginEvent event) {
        List<PolicyRule> activeRules = policyRuleRepository.findByActiveTrue();
        
        for (PolicyRule rule : activeRules) {
            if (evaluateRule(event, rule)) {
                createViolationRecord(event, rule);
            }
        }
    }
    
    private boolean evaluateRule(LoginEvent event, PolicyRule rule) {
        // Basic evaluation: Check for failed login attempts
        if ("FAILED".equals(event.getLoginStatus())) {
            return true;
        }
        
        // Add more complex rule evaluation logic here
        // For now, we'll keep it simple
        return false;
    }
    
    private void createViolationRecord(LoginEvent event, PolicyRule rule) {
        ViolationRecord violation = new ViolationRecord();
        violation.setUserId(event.getUserId());
        violation.setPolicyRuleId(rule.getId());
        violation.setEventId(event.getId());
        violation.setViolationType("LOGIN_VIOLATION");
        violation.setDetails("Policy violation detected: " + rule.getDescription());
        violation.setSeverity(rule.getSeverity());
        violation.setDetectedAt(LocalDateTime.now());
        violation.setResolved(false);
        
        violationRecordRepository.save(violation);
    }
}