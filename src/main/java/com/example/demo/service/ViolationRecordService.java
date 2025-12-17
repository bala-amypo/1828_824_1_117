package com.example.demo.service;

import com.example.demo.entity.ViolationRecord;
import java.util.List;

public interface ViolationRecordService {
    ViolationRecord saveViolation(ViolationRecord record);
    List<ViolationRecord> getViolationsByUserId(Long userId);
    List<ViolationRecord> getAllViolations();
}