@Service
public class ViolationRecordServiceImpl implements ViolationRecordService {
    private final ViolationRecordRepository violationRecordRepository;

    public ViolationRecordServiceImpl(ViolationRecordRepository violationRecordRepository) {
        this.violationRecordRepository = violationRecordRepository;
    }

    @Override
    public ViolationRecord markResolved(Long id) {
        ViolationRecord record = violationRecordRepository.findById(id).orElseThrow();
        record.setResolved(true);
        return violationRecordRepository.save(record);
    }

    @Override
    public List<ViolationRecord> getUnresolvedViolations() {
        return violationRecordRepository.findByResolvedFalse();
    }
}