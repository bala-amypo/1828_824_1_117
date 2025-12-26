package com.example.demo.repository;
import com.example.demo.entity.ViolationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ViolationRecordRepository extends JpaRepository<ViolationRecord, Long> {
    List<ViolationRecord> findByUserId(Long userId);
    List<ViolationRecord> findByResolvedFalse();
}