package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "violation_records", indexes = {
    @Index(name = "idx_user_id", columnList = "userId"),
    @Index(name = "idx_resolved", columnList = "resolved"),
    @Index(name = "idx_violation_time", columnList = "violationTime"),
    @Index(name = "idx_severity", columnList = "severity"),
    @Index(name = "idx_policy_rule", columnList = "policyRuleId")
})
public class ViolationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String ruleCode;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String severity; // LOW, MEDIUM, HIGH, CRITICAL

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(nullable = false)
    private LocalDateTime violationTime;

    private LocalDateTime resolutionTime;

    private String resolvedBy;

    private String resolutionNotes;

    @Column(nullable = false)
    private Boolean resolved = false;

    // NEW FIELDS ADDED
    private Long policyRuleId;
    private Long eventId;
    private String violationType; // LOGIN_VIOLATION, ACCESS_VIOLATION, etc.

    // Constructors
    public ViolationRecord() {
        this.violationTime = LocalDateTime.now();
        this.resolved = false;
    }

    public ViolationRecord(Long userId, String username, String ruleCode, String description, String severity) {
        this.userId = userId;
        this.username = username;
        this.ruleCode = ruleCode;
        this.description = description;
        this.severity = severity;
        this.violationTime = LocalDateTime.now();
        this.resolved = false;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRuleCode() { return ruleCode; }
    public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public LocalDateTime getViolationTime() { return violationTime; }
    public void setViolationTime(LocalDateTime violationTime) { this.violationTime = violationTime; }

    public LocalDateTime getResolutionTime() { return resolutionTime; }
    public void setResolutionTime(LocalDateTime resolutionTime) { this.resolutionTime = resolutionTime; }

    public String getResolvedBy() { return resolvedBy; }
    public void setResolvedBy(String resolvedBy) { this.resolvedBy = resolvedBy; }

    public String getResolutionNotes() { return resolutionNotes; }
    public void setResolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; }

    public Boolean getResolved() { return resolved; }
    public void setResolved(Boolean resolved) { this.resolved = resolved; }

    // NEW GETTERS AND SETTERS
    public Long getPolicyRuleId() { return policyRuleId; }
    public void setPolicyRuleId(Long policyRuleId) { this.policyRuleId = policyRuleId; }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public String getViolationType() { return violationType; }
    public void setViolationType(String violationType) { this.violationType = violationType; }
}