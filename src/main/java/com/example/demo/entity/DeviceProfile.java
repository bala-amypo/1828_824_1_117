package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
public class DeviceProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String deviceid;
    private boolean trusted;

    public String getDeviceid() { return deviceid; }
    public boolean isTrusted() { return trusted; }
    public void setTrusted(boolean trusted) { this.trusted = trusted; }
}