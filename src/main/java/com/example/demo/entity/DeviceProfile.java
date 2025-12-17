package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String deviceid; // lowercase 'id' to match your repository findByDeviceid
    private String deviceName;
    private String deviceType;
    private boolean trusted; // Use 'trusted' so Lombok generates setTrusted()
}