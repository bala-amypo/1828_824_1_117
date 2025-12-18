package com.example.demo.util;

import com.example.demo.entity.LoginEvent;
import com.example.demo.entity.ViolationRecord;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class RuleEvaluationUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public List<ViolationRecord> evaluateLoginEvent(LoginEvent event, List<com.example.demo.entity.PolicyRule> rules) {
        List<ViolationRecord> violations = new ArrayList<>();
        
        for (com.example.demo.entity.PolicyRule rule : rules) {
            if (rule.getActive() != null && !rule.getActive()) {
                continue; // Skip inactive rules
            }
            
            try {
                boolean isViolated = evaluateRule(event, rule);
                
                if (isViolated) {
                    ViolationRecord record = new ViolationRecord();
                    record.setUserId(event.getUserId());
                    record.setUsername(event.getUsername());
                    record.setRuleCode(rule.getRuleCode());
                    record.setDescription(rule.getDescription());
                    record.setSeverity(rule.getSeverity());
                    record.setDetails("Rule violation detected: " + rule.getDescription());
                    record.setViolationTime(LocalDateTime.now());
                    record.setResolved(false);
                    
                    // Set the new fields
                    record.setPolicyRuleId(rule.getId());
                    record.setEventId(event.getId());
                    record.setViolationType("LOGIN_VIOLATION");
                    
                    violations.add(record);
                }
            } catch (Exception e) {
                // Log error but continue with other rules
                System.err.println("Error evaluating rule " + rule.getRuleCode() + ": " + e.getMessage());
            }
        }
        
        return violations;
    }
    
    private boolean evaluateRule(LoginEvent event, com.example.demo.entity.PolicyRule rule) {
        if (rule.getConditionsJson() == null || rule.getConditionsJson().trim().isEmpty()) {
            return false;
        }
        
        try {
            JsonNode conditions = objectMapper.readTree(rule.getConditionsJson());
            
            for (JsonNode condition : conditions) {
                String field = condition.get("field").asText();
                String operator = condition.get("operator").asText();
                String value = condition.get("value").asText();
                
                if (!evaluateCondition(event, field, operator, value)) {
                    return false; // All conditions must be satisfied (AND logic)
                }
            }
            
            return true; // All conditions satisfied
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse rule conditions: " + e.getMessage(), e);
        }
    }
    
    private boolean evaluateCondition(LoginEvent event, String field, String operator, String value) {
        String actualValue = getFieldValue(event, field);
        
        if (actualValue == null) {
            return false;
        }
        
        switch (operator.toUpperCase()) {
            case "EQUALS":
                return actualValue.equalsIgnoreCase(value);
                
            case "NOT_EQUALS":
                return !actualValue.equalsIgnoreCase(value);
                
            case "CONTAINS":
                return actualValue.toLowerCase().contains(value.toLowerCase());
                
            case "NOT_CONTAINS":
                return !actualValue.toLowerCase().contains(value.toLowerCase());
                
            case "STARTS_WITH":
                return actualValue.toLowerCase().startsWith(value.toLowerCase());
                
            case "ENDS_WITH":
                return actualValue.toLowerCase().endsWith(value.toLowerCase());
                
            case "GREATER_THAN":
                return compareNumbers(actualValue, value) > 0;
                
            case "LESS_THAN":
                return compareNumbers(actualValue, value) < 0;
                
            case "GREATER_THAN_EQUALS":
                return compareNumbers(actualValue, value) >= 0;
                
            case "LESS_THAN_EQUALS":
                return compareNumbers(actualValue, value) <= 0;
                
            case "IN":
                return isInList(actualValue, value);
                
            case "NOT_IN":
                return !isInList(actualValue, value);
                
            case "BETWEEN":
                return isBetween(actualValue, value);
                
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }
    
    private String getFieldValue(LoginEvent event, String field) {
        switch (field.toLowerCase()) {
            case "status":
                return event.getStatus();
                
            case "loginstatus":
                return event.getLoginStatus();
                
            case "deviceid":
                return event.getDeviceId();
                
            case "ipaddress":
                return event.getIpAddress();
                
            case "username":
                return event.getUsername();
                
            case "userid":
                return event.getUserId() != null ? event.getUserId().toString() : null;
                
            case "useragent":
                return event.getUserAgent();
                
            case "location":
                return event.getLocation();
                
            case "deviceinfo":
                return event.getDeviceInfo();
                
            case "failureReason":
            case "failure_reason":
                return event.getFailureReason();
                
            case "suspicious":
                return event.getSuspicious() != null ? event.getSuspicious().toString() : null;
                
            case "eventtime":
            case "event_time":
                return event.getEventTime() != null ? 
                       event.getEventTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
                
            default:
                return null;
        }
    }
    
    private int compareNumbers(String value1, String value2) {
        try {
            double num1 = Double.parseDouble(value1);
            double num2 = Double.parseDouble(value2);
            return Double.compare(num1, num2);
        } catch (NumberFormatException e) {
            // If not numbers, compare as strings
            return value1.compareTo(value2);
        }
    }
    
    private boolean isInList(String value, String list) {
        String[] items = list.split(",");
        for (String item : items) {
            if (value.trim().equalsIgnoreCase(item.trim())) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isBetween(String value, String range) {
        String[] bounds = range.split(",");
        if (bounds.length != 2) {
            return false;
        }
        
        try {
            double num = Double.parseDouble(value);
            double lower = Double.parseDouble(bounds[0].trim());
            double upper = Double.parseDouble(bounds[1].trim());
            return num >= lower && num <= upper;
        } catch (NumberFormatException e) {
            // If not numbers, compare as dates
            try {
                LocalDateTime date = LocalDateTime.parse(value.trim());
                LocalDateTime lowerDate = LocalDateTime.parse(bounds[0].trim());
                LocalDateTime upperDate = LocalDateTime.parse(bounds[1].trim());
                return (date.isEqual(lowerDate) || date.isAfter(lowerDate)) &&
                       (date.isEqual(upperDate) || date.isBefore(upperDate));
            } catch (Exception ex) {
                return false;
            }
        }
    }
    
    // Helper method to check for suspicious patterns
    public boolean isSuspiciousLoginPattern(LoginEvent event, List<LoginEvent> recentEvents) {
        // Check for multiple failed attempts
        long failedAttempts = recentEvents.stream()
                .filter(e -> "FAILED".equals(e.getStatus()))
                .count();
        
        if (failedAttempts >= 5) {
            event.setSuspicious(true);
            event.setSuspicionReason("Multiple failed login attempts");
            return true;
        }
        
        // Check for different locations in short time
        if (event.getLocation() != null && recentEvents.size() > 1) {
            String currentLocation = event.getLocation();
            long differentLocations = recentEvents.stream()
                    .filter(e -> e.getLocation() != null && !e.getLocation().equals(currentLocation))
                    .count();
            
            if (differentLocations > 2) {
                event.setSuspicious(true);
                event.setSuspicionReason("Login from multiple locations in short time");
                return true;
            }
        }
        
        // Check for different devices
        if (event.getDeviceId() != null && recentEvents.size() > 1) {
            String currentDevice = event.getDeviceId();
            long differentDevices = recentEvents.stream()
                    .filter(e -> e.getDeviceId() != null && !e.getDeviceId().equals(currentDevice))
                    .count();
            
            if (differentDevices > 2) {
                event.setSuspicious(true);
                event.setSuspicionReason("Login from multiple devices in short time");
                return true;
            }
        }
        
        return false;
    }
}