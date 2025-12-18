package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "device_profiles")
public class DeviceProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deviceid;
    private String deviceName;
    private String deviceType;
    private boolean trusted;

    // 1. Default No-Args Constructor (Required by JPA/Hibernate)
    public DeviceProfile() {
    }

    // 2. Full Parameterized Constructor
    public DeviceProfile(Long id, String deviceid, String deviceName, String deviceType, boolean trusted) {
        this.id = id;
        this.deviceid = deviceid;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.trusted = trusted;
    }

    // 3. Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public boolean isTrusted() {
        return trusted;
    }

    public void setTrusted(boolean trusted) {
        this.trusted = trusted;
    }
}