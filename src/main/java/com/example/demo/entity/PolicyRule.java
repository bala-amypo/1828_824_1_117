package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "policy_rules", uniqueConstraints = {
    @UniqueConstraint(columnNames = "ruleCode")
})
public class PolicyRule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String ruleCode;
    
    private String description;
    
    @Column(nullable = false)
    private String severity; // LOW, MEDIUM, HIGH, CRITICAL
    
    @Column(columnDefinition = "TEXT")
    private String conditionsJson;
    
    @Column(nullable = false)
    private Boolean active = true;
}