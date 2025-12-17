package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "device_profiles")
@Data
public class DeviceProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String deviceId;

    private String deviceType;
    private String osVersion;
    private Boolean isTrusted = false;
    private LocalDateTime lastSeen;
}
