package com.example.demo.service;

import com.example.demo.entity.DeviceProfile;
import java.util.List;

public interface DeviceProfileService {
    DeviceProfile registerDevice(DeviceProfile device);
    List<DeviceProfile> getDevicesByUser(Long userId);
    DeviceProfile findByDeviceId(String deviceId);
}