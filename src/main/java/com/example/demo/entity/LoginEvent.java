package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "login_events", indexes = {
    @Index(name = "idx_user_id", columnList = "userId"),
    @Index(name = "idx_event_time", columnList = "eventTime"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_ip_address", columnList = "ipAddress")
})
public class LoginEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String ipAddress;

    private String userAgent;

    @Column(nullable = false)
    private LocalDateTime eventTime;

    @Column(nullable = false)
    private String status; // SUCCESS, FAILED, LOCKED, etc.

    private String failureReason;

    // Additional fields
    private String deviceId;
    private String location;
    private String deviceInfo;
    private Boolean suspicious = false;
    private String suspicionReason;
    private String loginStatus; // ACTIVE, INACTIVE, LOCKED

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

    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }

    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDeviceInfo() { return deviceInfo; }
    public void setDeviceInfo(String deviceInfo) { this.deviceInfo = deviceInfo; }

    public Boolean getSuspicious() { return suspicious; }
    public void setSuspicious(Boolean suspicious) { this.suspicious = suspicious; }

    public String getSuspicionReason() { return suspicionReason; }
    public void setSuspicionReason(String suspicionReason) { this.suspicionReason = suspicionReason; }

    public String getLoginStatus() { return loginStatus; }
    public void setLoginStatus(String loginStatus) { this.loginStatus = loginStatus; }
}