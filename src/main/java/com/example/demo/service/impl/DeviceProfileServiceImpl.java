package com.example.demo.service.impl;

import com.example.demo.entity.DeviceProfile;
import com.example.demo.repository.DeviceProfileRepository;
import com.example.demo.service.DeviceProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DeviceProfileServiceImpl implements DeviceProfileService {
    
    private final DeviceProfileRepository deviceProfileRepository;
    
    // EXACT CONSTRUCTOR SIGNATURE AS REQUIRED
    public DeviceProfileServiceImpl(DeviceProfileRepository deviceProfileRepository) {
        this.deviceProfileRepository = deviceProfileRepository;
    }
    
    @Override
    public DeviceProfile registerDevice(DeviceProfile device) {
        // Check if device already exists for this user
        List<DeviceProfile> existingDevices = deviceProfileRepository.findByUserId(device.getUserId());
        for (DeviceProfile existing : existingDevices) {
            if (existing.getDeviceId().equals(device.getDeviceId())) {
                throw new IllegalArgumentException("Device ID already registered for this user");
            }
        }
        
        return deviceProfileRepository.save(device);
    }
    
    @Override
    public DeviceProfile updateTrustStatus(Long id, boolean trust) {
        DeviceProfile device = deviceProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Device not found"));
        device.setIsTrusted(trust);
        return deviceProfileRepository.save(device);
    }
    
    @Override
    public List<DeviceProfile> getDevicesByUser(Long userId) {
        return deviceProfileRepository.findByUserId(userId);
    }
    
    @Override
    public Optional<DeviceProfile> findByDeviceId(String deviceId) {
        return deviceProfileRepository.findByDeviceId(deviceId);
    }
}