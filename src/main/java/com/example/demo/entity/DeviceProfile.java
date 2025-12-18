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
@Table(name = "device_profiles")
public class DeviceProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private String deviceId;
    
    private String deviceType;
    
    private String osVersion;
    
    @Column(nullable = false)
    private LocalDateTime lastSeen;
    
    @Column(nullable = false)
    private Boolean isTrusted = false;
    
    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastSeen = LocalDateTime.now();
    }
}