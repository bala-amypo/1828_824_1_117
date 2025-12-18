package com.example.demo.controller;

import com.example.demo.entity.DeviceProfile;
import com.example.demo.service.DeviceProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Device Profiles", description = "Device profile management")
@RestController
@RequestMapping("/api/devices")
public class DeviceProfileController {
    
    private final DeviceProfileService deviceProfileService;
    
    public DeviceProfileController(DeviceProfileService deviceProfileService) {
        this.deviceProfileService = deviceProfileService;
    }
    
    @Operation(summary = "Register device")
    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<DeviceProfile> registerDevice(@RequestBody DeviceProfile device) {
        DeviceProfile registeredDevice = deviceProfileService.registerDevice(device);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredDevice);
    }
    
    @Operation(summary = "Update trusted status")
    @PutMapping("/{id}/trust")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DeviceProfile> updateTrustStatus(@PathVariable Long id,
                                                          @RequestParam boolean trust) {
        DeviceProfile updatedDevice = deviceProfileService.updateTrustStatus(id, trust);
        return ResponseEntity.ok(updatedDevice);
    }
    
    @Operation(summary = "Get devices for user")
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<DeviceProfile>> getDevicesByUser(@PathVariable Long userId) {
        List<DeviceProfile> devices = deviceProfileService.getDevicesByUser(userId);
        return ResponseEntity.ok(devices);
    }
    
    @Operation(summary = "Find device by device ID")
    @GetMapping("/lookup/{deviceId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
    public ResponseEntity<DeviceProfile> findByDeviceId(@PathVariable String deviceId) {
        return deviceProfileService.findByDeviceId(deviceId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}