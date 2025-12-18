package com.example.demo.service.impl;

import com.example.demo.entity.ViolationRecord;
import com.example.demo.repository.ViolationRecordRepository;
import com.example.demo.service.ViolationRecordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ViolationRecordServiceImpl implements ViolationRecordService {

    private final ViolationRecordRepository violationRecordRepository;

    public ViolationRecordServiceImpl(ViolationRecordRepository violationRecordRepository) {
        this.violationRecordRepository = violationRecordRepository;
    }

    @Override
    @Transactional
    public ViolationRecord logViolation(ViolationRecord record) {
        if (record.getViolationTime() == null) {
            record.setViolationTime(LocalDateTime.now());
        }
        if (record.getResolved() == null) {
            record.setResolved(false);
        }
        return violationRecordRepository.save(record);
    }

    @Override
    public ViolationRecord getViolationById(Long id) {
        return violationRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Violation not found with id: " + id));
    }

    @Override
    public List<ViolationRecord> getAllViolations() {
        return violationRecordRepository.findAll();
    }

    @Override
    public Page<ViolationRecord> getAllViolations(Pageable pageable) {
        return violationRecordRepository.findAll(pageable);
    }

    @Override
    public List<ViolationRecord> getViolationsByUser(Long userId) {
        return violationRecordRepository.findByUserId(userId);
    }

    @Override
    public List<ViolationRecord> getViolationsByUser(String username) {
        return violationRecordRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public ViolationRecord markResolved(Long id) {
        return violationRecordRepository.findById(id)
                .map(violation -> {
                    violation.setResolved(true);
                    violation.setResolutionTime(LocalDateTime.now());
                    return violationRecordRepository.save(violation);
                })
                .orElseThrow(() -> new RuntimeException("Violation not found with id: " + id));
    }

    @Override
    @Transactional
    public ViolationRecord markUnresolved(Long id) {
        return violationRecordRepository.findById(id)
                .map(violation -> {
                    violation.setResolved(false);
                    violation.setResolutionTime(null);
                    return violationRecordRepository.save(violation);
                })
                .orElseThrow(() -> new RuntimeException("Violation not found with id: " + id));
    }

    @Override
    public List<ViolationRecord> getUnresolvedViolations() {
        return violationRecordRepository.findByResolvedFalse();
    }

    @Override
    public List<ViolationRecord> getResolvedViolations() {
        return violationRecordRepository.findByResolvedTrue();
    }

    @Override
    public List<ViolationRecord> getViolationsBySeverity(String severity) {
        return violationRecordRepository.findBySeverity(severity);
    }

    @Override
    public List<ViolationRecord> getViolationsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Both startDate and endDate must be provided");
        }
        return violationRecordRepository.findByViolationTimeBetween(startDate, endDate);
    }

    @Override
    public Long countUnresolvedViolations() {
        return violationRecordRepository.countByResolvedFalse();
    }

    @Override
    public Long countViolationsByUser(Long userId) {
        return violationRecordRepository.countByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteViolation(Long id) {
        violationRecordRepository.deleteById(id);
    }
}