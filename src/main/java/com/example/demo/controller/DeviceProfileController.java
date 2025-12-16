package com.example.demo.controller;

import com.example.demo.entity.DeviceProfile;
import com.example.demo.service.DeviceProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Device Profiles", description = "Endpoints for managing user devices")
@RestController
@RequestMapping("/api/devices")
public class DeviceProfileController {

    private final DeviceProfileService deviceProfileService;

    // CONSTRUCTOR INJECTION
    public DeviceProfileController(DeviceProfileService deviceProfileService) {
        this.deviceProfileService = deviceProfileService;
    }

    // POST /api/devices/ - Register device
    @PostMapping("/")
    public ResponseEntity<DeviceProfile> registerDevice(@RequestBody DeviceProfile device) {
        DeviceProfile newDevice = deviceProfileService.registerDevice(device);
        return new ResponseEntity<>(newDevice, HttpStatus.CREATED);
    }

    // PUT /api/devices/{id}/trust - Update trusted status (Role: ADMIN)
    @PutMapping("/{id}/trust")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<DeviceProfile> updateTrustedStatus(@PathVariable Long id, @RequestParam boolean trust) {
        DeviceProfile updatedDevice = deviceProfileService.updateTrustStatus(id, trust);
        return ResponseEntity.ok(updatedDevice);
    }

    // GET /api/devices/user/{userId} - Get all devices for user
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<DeviceProfile>> getDevicesByUser(@PathVariable Long userId) {
        List<DeviceProfile> devices = deviceProfileService.getDevicesByUser(userId);
        return ResponseEntity.ok(devices);
    }

    // GET /api/devices/lookup/{deviceId} - Find device by ID (unique per user)
    @GetMapping("/lookup/{deviceId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<DeviceProfile> findDeviceById(@PathVariable String deviceId) {
        DeviceProfile device = deviceProfileService.findByDeviceId(deviceId);
        return ResponseEntity.ok(device);
    }
}