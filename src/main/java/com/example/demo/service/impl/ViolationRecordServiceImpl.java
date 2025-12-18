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