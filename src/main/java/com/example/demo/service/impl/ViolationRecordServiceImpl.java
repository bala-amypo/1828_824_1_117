package com.example.demo.service.impl;
import com.example.demo.entity.ViolationRecord;
import com.example.demo.repository.ViolationRecordRepository;
import com.example.demo.service.ViolationRecordService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ViolationRecordServiceImpl implements ViolationRecordService {
    private final ViolationRecordRepository repo;
    public ViolationRecordServiceImpl(ViolationRecordRepository repo) { this.repo = repo; }

    @Override public ViolationRecord logViolation(ViolationRecord v) { return repo.save(v); }
    @Override public List<ViolationRecord> getViolationsByUser(Long userId) { return repo.findByUserId(userId); }
    @Override public List<ViolationRecord> getUnresolvedViolations() { return repo.findByResolvedFalse(); }
    @Override public List<ViolationRecord> getAllViolations() { return repo.findAll(); }
    @Override public ViolationRecord markResolved(Long id) {
        return repo.findById(id).map(v -> {
            v.setResolved(true);
            return repo.save(v);
        }).orElse(null);
    }
}