package com.example.demo.service;

import com.example.demo.entity.ViolationRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ViolationRecordService {
    ViolationRecord logViolation(ViolationRecord record);
    ViolationRecord getViolationById(Long id);
    List<ViolationRecord> getAllViolations();
    Page<ViolationRecord> getAllViolations(Pageable pageable);
    List<ViolationRecord> getViolationsByUser(Long userId);
    List<ViolationRecord> getViolationsByUser(String username);
    ViolationRecord markResolved(Long id);
    ViolationRecord markUnresolved(Long id);
    List<ViolationRecord> getUnresolvedViolations();
    List<ViolationRecord> getResolvedViolations();
    List<ViolationRecord> getViolationsBySeverity(String severity);
    List<ViolationRecord> getViolationsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    Long countUnresolvedViolations();
    Long countViolationsByUser(Long userId);
    void deleteViolation(Long id);
}