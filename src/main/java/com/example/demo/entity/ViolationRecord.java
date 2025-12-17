package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "violation_records")
@Data
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

    private String violationType;
    private String details;
    private String severity;
    private LocalDateTime detectedAt = LocalDateTime.now();
    private Boolean resolved = false;
}
