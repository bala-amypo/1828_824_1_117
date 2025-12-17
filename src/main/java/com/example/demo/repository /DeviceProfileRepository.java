package com.example.demo.repository;

import com.example.demo.entity.DeviceProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceProfileRepository extends JpaRepository<DeviceProfile, Long> {
    // Note: "deviceid" as per your specific technical instructions
    DeviceProfile findByDeviceid(String deviceId);
}