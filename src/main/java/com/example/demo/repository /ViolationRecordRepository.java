package com.example.demo.repository;

import com.example.demo.entity.ViolationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ViolationRecordRepository extends JpaRepository<ViolationRecord, Long> {

    // Basic queries
    List<ViolationRecord> findByUserId(Long userId);
    
    @Query("SELECT v FROM ViolationRecord v WHERE v.username = :username")
    List<ViolationRecord> findByUsername(@Param("username") String username);
    
    List<ViolationRecord> findBySeverity(String severity);
    List<ViolationRecord> findByResolvedFalse();
    List<ViolationRecord> findByResolvedTrue();
    Long countByResolvedFalse();
    Long countByUserId(Long userId);
    
    List<ViolationRecord> findByViolationTimeBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT v FROM ViolationRecord v WHERE v.userId = :userId AND v.violationTime BETWEEN :start AND :end")
    List<ViolationRecord> findByUserIdAndViolationTimeBetween(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
    
    List<ViolationRecord> findByRuleCode(String ruleCode);
    
    @Query("SELECT v FROM ViolationRecord v WHERE v.resolved = false AND v.severity IN ('HIGH', 'CRITICAL')")
    List<ViolationRecord> findHighPriorityViolations();
}