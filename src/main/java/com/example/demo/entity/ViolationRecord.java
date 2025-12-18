package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "violation_records")
public class ViolationRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private Long policyRuleId;
    
    @Column(nullable = false)
    private Long eventId;
    
    @Column(nullable = false)
    private String violationType;
    
    @Column(columnDefinition = "TEXT")
    private String details;
    
    @Column(nullable = false)
    private String severity; // Inherited from PolicyRule
    
    @Column(nullable = false)
    private LocalDateTime detectedAt = LocalDateTime.now();
    
    @Column(nullable = false)
    private Boolean resolved = false;
}