package com.example.demo.controller;

import com.example.demo.entity.DeviceProfile;
import com.example.demo.service.DeviceProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class DeviceProfileController {

    private final DeviceProfileService deviceProfileService;

    public DeviceProfileController(DeviceProfileService deviceProfileService) {
        this.deviceProfileService = deviceProfileService;
    }

    @PostMapping("/register")
    public ResponseEntity<DeviceProfile> registerDevice(@RequestBody DeviceProfile device) {
        // Fixes error: cannot find symbol method registerDevice
        return ResponseEntity.ok(deviceProfileService.registerDevice(device));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DeviceProfile>> getDevicesByUser(@PathVariable Long userId) {
        // Fixes error: cannot find symbol method getDevicesByUser
        return ResponseEntity.ok(deviceProfileService.getDevicesByUser(userId));
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<DeviceProfile> getDeviceById(@PathVariable String deviceId) {
        // Fixes error: incompatible types: Long cannot be converted to String
        // Here we ensure the PathVariable is a String to match the service method
        DeviceProfile device = deviceProfileService.findByDeviceId(deviceId);
        return device != null ? ResponseEntity.ok(device) : ResponseEntity.notFound().build();
    }
}