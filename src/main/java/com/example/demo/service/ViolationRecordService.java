package com.example.demo.service;

import com.example.demo.entity.ViolationRecord;
import java.util.List;

public interface ViolationRecordService {
    ViolationRecord logViolation(ViolationRecord record);
    List<ViolationRecord> getViolationsByUser(Long userId);
    ViolationRecord markResolved(Long id);
    List<ViolationRecord> getUnresolvedViolations();
    List<ViolationRecord> getAllViolations();
}