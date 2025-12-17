package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "login_events")
@Data
public class LoginEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String ipAddress;

    private String location;

    @Column(nullable = false)
    private String deviceId;

    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(nullable = false)
    private String loginStatus; // SUCCESS, FAILED
}
