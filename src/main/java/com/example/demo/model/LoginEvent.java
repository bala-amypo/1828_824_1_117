package com.example.demo.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class LoginEvent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String deviceId;
    private String ipAddress;
    private String location;
    private String loginStatus;
    private LocalDateTime timestamp;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getLoginStatus() { return loginStatus; }
    public void setLoginStatus(String loginStatus) { this.loginStatus = loginStatus; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}