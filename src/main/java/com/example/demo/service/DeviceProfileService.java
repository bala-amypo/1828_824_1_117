package com.example.demo.service;

import com.example.demo.entity.DeviceProfile;
import java.util.List;
import java.util.Optional;

public interface DeviceProfileService {
    DeviceProfile registerDevice(DeviceProfile device);
    DeviceProfile updateTrustStatus(Long id, boolean trust);
    List<DeviceProfile> getDevicesByUser(Long userId);
    Optional<DeviceProfile> findByDeviceId(String deviceId);
}