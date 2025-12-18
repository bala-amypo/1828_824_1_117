package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "violation_records")
public class ViolationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long policyRuleId;   // Fixes setPolicyRuleId error
    private Long eventId;        // Fixes setEventId error
    private String violationType; // Fixes setViolationType error
    private String severity;
    private String ruleCode;
    
    @Column(columnDefinition = "TEXT")
    private String details;
    
    private Boolean resolved = false;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ViolationRecord() {}

    // Add these specific Getters/Setters to fix RuleEvaluationUtil errors
    public Long getPolicyRuleId() { return policyRuleId; }
    public void setPolicyRuleId(Long policyRuleId) { this.policyRuleId = policyRuleId; }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public String getViolationType() { return violationType; }
    public void setViolationType(String violationType) { this.violationType = violationType; }

    // Standard Getters/Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getRuleCode() { return ruleCode; }
    public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public Boolean getResolved() { return resolved; }
    public void setResolved(Boolean resolved) { this.resolved = resolved; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}