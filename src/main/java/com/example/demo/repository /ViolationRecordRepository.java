package com.example.demo.repository;

import com.example.demo.entity.ViolationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ViolationRecordRepository extends JpaRepository<ViolationRecord, Long> {
    // Required to fix the "cannot find symbol" error
    List<ViolationRecord> findByUserId(Long userId);

    // Required for Priority 23 (getUnresolvedViolations)
    List<ViolationRecord> findByResolvedFalse(); 
}