package com.example.demo.service.impl;

import com.example.demo.entity.DeviceProfile;
import com.example.demo.repository.DeviceProfileRepository;
import com.example.demo.service.DeviceProfileService;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DeviceProfileServiceImpl implements DeviceProfileService {

    private final DeviceProfileRepository deviceRepository;

    public DeviceProfileServiceImpl(DeviceProfileRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public DeviceProfile saveDevice(DeviceProfile device) {
        return deviceRepository.save(device);
    }

    @Override
    public DeviceProfile getDeviceById(String deviceId) {
        DeviceProfile device = deviceRepository.findByDeviceid(deviceId);
        if (device == null) {
            throw new ResourceNotFoundException("Device not found with ID: " + deviceId);
        }
        return device;
    }

    @Override
    public DeviceProfile updateTrustStatus(String deviceId, boolean isTrusted) {
        DeviceProfile device = getDeviceById(deviceId);
        device.setTrusted(isTrusted); 
        return deviceRepository.save(device);
    }

    @Override
    public List<DeviceProfile> getAllDevices() {
        return deviceRepository.findAll();
    }
}