package com.example.demo.service;

import com.example.demo.entity.DeviceProfile;
import java.util.List;

public interface DeviceProfileService {
    DeviceProfile saveDevice(DeviceProfile device);
    DeviceProfile getDeviceById(String deviceId);
    DeviceProfile updateTrustStatus(String deviceId, boolean isTrusted);
    List<DeviceProfile> getAllDevices();
}