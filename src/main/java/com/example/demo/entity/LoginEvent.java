package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "login_events")
public class LoginEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String username;

    private String ipAddress;

    private String userAgent;

    @Column(nullable = false)
    private LocalDateTime eventTime;

    private String status; // SUCCESS, FAILED, SUSPICIOUS, etc.

    private String location;

    private String deviceInfo;

    private Boolean suspicious = false;

    private String suspicionReason;

    // Constructors
    public LoginEvent() {
        this.eventTime = LocalDateTime.now();
    }

    public LoginEvent(Long userId, String username, String ipAddress, String userAgent, String status) {
        this.userId = userId;
        this.username = username;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.status = status;
        this.eventTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    public LocalDateTime getEventTime() { return eventTime; }
    public void setEventTime(LocalDateTime eventTime) { this.eventTime = eventTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDeviceInfo() { return deviceInfo; }
    public void setDeviceInfo(String deviceInfo) { this.deviceInfo = deviceInfo; }

    public Boolean getSuspicious() { return suspicious; }
    public void setSuspicious(Boolean suspicious) { this.suspicious = suspicious; }

    public String getSuspicionReason() { return suspicionReason; }
    public void setSuspicionReason(String suspicionReason) { this.suspicionReason = suspicionReason; }
}