package com.example.demo.service.impl;

import com.example.demo.entity.DeviceProfile;
import com.example.demo.repository.DeviceProfileRepository;
import com.example.demo.service.DeviceProfileService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DeviceProfileServiceImpl implements DeviceProfileService {

    private final DeviceProfileRepository deviceProfileRepository;

    // STEP 0.5 - Exact Constructor order
    public DeviceProfileServiceImpl(DeviceProfileRepository deviceProfileRepository) {
        this.deviceProfileRepository = deviceProfileRepository;
    }

    @Override
    public DeviceProfile registerDevice(DeviceProfile device) {
        device.setLastSeen(LocalDateTime.now());
        return deviceProfileRepository.save(device);
    }

    @Override
    public DeviceProfile updateTrustStatus(Long id, boolean trust) {
        return deviceProfileRepository.findById(id).map(d -> {
            d.setIsTrusted(trust);
            return deviceProfileRepository.save(d);
        }).orElse(null);
    }

    @Override
    public List<DeviceProfile> getDevicesByUser(Long userId) {
        return deviceProfileRepository.findByUserId(userId);
    }

    @Override
    public Optional<DeviceProfile> findByDeviceId(String deviceId) {
        // STEP 1 - Exact repository method naming
        return deviceProfileRepository.findByDeviceId(deviceId);
    }
}