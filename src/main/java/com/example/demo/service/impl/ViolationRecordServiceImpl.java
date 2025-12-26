package com.example.demo.service.impl;

import com.example.demo.entity.ViolationRecord;
import com.example.demo.repository.ViolationRecordRepository;
import com.example.demo.service.ViolationRecordService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ViolationRecordServiceImpl implements ViolationRecordService {

    private final ViolationRecordRepository violationRecordRepository;

    // STEP 0.5 - Mandatory Constructor Signature
    public ViolationRecordServiceImpl(ViolationRecordRepository violationRecordRepository) {
        this.violationRecordRepository = violationRecordRepository;
    }

    @Override
    public ViolationRecord logViolation(ViolationRecord violation) {
        // Step 1.5 - Ensure resolved defaults to false if null
        if (violation.getResolved() == null) {
            violation.setResolved(false);
        }
        return violationRecordRepository.save(violation);
    }

    @Override
    public List<ViolationRecord> getViolationsByUser(Long userId) {
        return violationRecordRepository.findByUserId(userId);
    }

    @Override
    public ViolationRecord markResolved(Long id) {
        // Priority 24: Find by ID and toggle resolved flag
        return violationRecordRepository.findById(id).map(v -> {
            v.setResolved(true);
            return violationRecordRepository.save(v);
        }).orElse(null);
    }

    @Override
    public List<ViolationRecord> getUnresolvedViolations() {
        return violationRecordRepository.findByResolvedFalse();
    }

    @Override
    public List<ViolationRecord> getAllViolations() {
        return violationRecordRepository.findAll();
    }
}