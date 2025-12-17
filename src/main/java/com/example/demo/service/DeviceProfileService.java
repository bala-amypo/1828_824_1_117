package com.example.demo.service.impl;

import com.example.demo.entity.DeviceProfile;
import com.example.demo.repository.DeviceProfileRepository;
import com.example.demo.service.DeviceProfileService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DeviceProfileServiceImpl implements DeviceProfileService {
    private final DeviceProfileRepository deviceProfileRepository;

    public DeviceProfileServiceImpl(DeviceProfileRepository deviceProfileRepository) {
        this.deviceProfileRepository = deviceProfileRepository;
    }

    @Override
    public DeviceProfile registerDevice(DeviceProfile device) {
        return deviceProfileRepository.save(device);
    }

    @Override
    public DeviceProfile updateTrustStatus(Long id, boolean trust) {
        DeviceProfile device = deviceProfileRepository.findById(id).orElseThrow();
        device.setIsTrusted(trust);
        return deviceProfileRepository.save(device);
    }

    @Override
    public List<DeviceProfile> getDevicesByUser(Long userId) {
        // Assume repository has findByUserId
        return deviceProfileRepository.findAll(); 
    }

    @Override
    public DeviceProfile findByDeviceId(String deviceId) {
        return deviceProfileRepository.findByDeviceid(deviceId);
    }
}