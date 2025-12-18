package com.example.demo.util;

import com.example.demo.entity.LoginEvent;
import com.example.demo.entity.PolicyRule;
import com.example.demo.entity.ViolationRecord;
import com.example.demo.repository.PolicyRuleRepository;
import com.example.demo.repository.ViolationRecordRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class RuleEvaluationUtil {
    
    private final PolicyRuleRepository policyRuleRepository;
    private final ViolationRecordRepository violationRecordRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
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
        if (rule.getConditionsJson() == null || rule.getConditionsJson().isEmpty()) {
            return false;
        }
        
        try {
            JsonNode conditions = objectMapper.readTree(rule.getConditionsJson());
            // Simple evaluation logic - you can expand this
            return evaluateConditions(event, conditions);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean evaluateConditions(LoginEvent event, JsonNode conditions) {
        // Simple evaluation: check if login status is FAILED
        if (conditions.has("loginStatus")) {
            String requiredStatus = conditions.get("loginStatus").asText();
            return requiredStatus.equals(event.getLoginStatus());
        }
        
        // Add more condition evaluations as needed
        return false;
    }
    
    private void createViolationRecord(LoginEvent event, PolicyRule rule) {
        ViolationRecord violation = new ViolationRecord();
        violation.setUserId(event.getUserId());
        violation.setPolicyRuleId(rule.getId());
        violation.setEventId(event.getId());
        violation.setViolationType("LOGIN_VIOLATION");
        violation.setDetails("Policy rule violated: " + rule.getDescription());
        violation.setSeverity(rule.getSeverity());
        violation.setDetectedAt(LocalDateTime.now());
        violation.setResolved(false);
        
        violationRecordRepository.save(violation);
    }
}