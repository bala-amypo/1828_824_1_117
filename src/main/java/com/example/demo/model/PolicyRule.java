package com.example.demo.entity;
import jakarta.persistence.*;

@Entity
public class PolicyRule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ruleCode;
    private String description;
    private String severity;
    private String conditionsJson;
    private Boolean active;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRuleCode() { return ruleCode; }
    public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getConditionsJson() { return conditionsJson; }
    public void setConditionsJson(String conditionsJson) { this.conditionsJson = conditionsJson; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}