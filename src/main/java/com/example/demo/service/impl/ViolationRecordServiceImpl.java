package com.example.demo.service.impl;

import com.example.demo.entity.ViolationRecord;
import com.example.demo.repository.ViolationRecordRepository;
import com.example.demo.service.ViolationRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
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
        ViolationRecord violation = violationRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Violation not found"));
        violation.setResolved(true);
        return violationRecordRepository.save(violation);
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