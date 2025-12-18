package com.example.demo.service.impl;

import com.example.demo.entity.DeviceProfile;
import com.example.demo.repository.DeviceProfileRepository;
import com.example.demo.service.DeviceProfileService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DeviceProfileServiceImpl implements DeviceProfileService {

    private final DeviceProfileRepository repository;

    public DeviceProfileServiceImpl(DeviceProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public DeviceProfile registerDevice(DeviceProfile device) {
        return repository.save(device);
    }

    @Override
    public List<DeviceProfile> getDevicesByUser(Long userId) {
        // Ensure your repository has a findByUserId method if needed, 
        // or filter accordingly.
        return repository.findAll().stream()
                .filter(d -> d.getId().equals(userId)) // Example logic
                .toList();
    }

    @Override
    public DeviceProfile findByDeviceId(String deviceId) {
        // Matches the Controller's call
        return repository.findByDeviceid(deviceId);
    }
}