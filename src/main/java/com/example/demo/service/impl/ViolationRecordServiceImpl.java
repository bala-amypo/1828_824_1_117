package com.example.demo.service.impl;

import com.example.demo.entity.ViolationRecord;
import com.example.demo.repository.ViolationRecordRepository;
import com.example.demo.service.ViolationRecordService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ViolationRecordServiceImpl implements ViolationRecordService {

    private final ViolationRecordRepository violationRecordRepository;

    
    public ViolationRecordServiceImpl(ViolationRecordRepository violationRecordRepository) {
        this.violationRecordRepository = violationRecordRepository;
    }

    @Override
    public ViolationRecord logViolation(ViolationRecord record) {
        return violationRecordRepository.save(record);
    }

    @Override
    public List<ViolationRecord> getViolationsByUser(Long userId) {
        return violationRecordRepository.findByUserId(userId);
    }

    @Override
    public ViolationRecord markResolved(Long id) {
        ViolationRecord record = violationRecordRepository.findById(id).orElse(null);
        if (record != null) {
            record.setResolved(true);
            return violationRecordRepository.save(record);
        }
        return null;
    }

    @Override
    public List<ViolationRecord> getUnresolvedViolations() {
        return violationRecordRepository.findByResolvedFalse();
    }
}