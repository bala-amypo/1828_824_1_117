package com.example.demo.repository;

import com.example.demo.entity.ViolationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ViolationRecordRepository extends JpaRepository<ViolationRecord, Long> {
    List<ViolationRecord> findByResolvedFalse();
}