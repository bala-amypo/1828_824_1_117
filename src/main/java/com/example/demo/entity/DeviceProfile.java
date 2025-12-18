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
@Table(name = "device_profiles", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"userId", "deviceId"})
})
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
    private LocalDateTime lastSeen = LocalDateTime.now();
    
    @Column(nullable = false)
    private Boolean isTrusted = false;
}