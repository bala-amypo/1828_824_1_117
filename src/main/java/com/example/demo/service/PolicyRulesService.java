@Service
public class PolicyRuleServiceImpl implements PolicyRuleService {
    private final PolicyRuleRepository policyRuleRepository;

    public PolicyRuleServiceImpl(PolicyRuleRepository policyRuleRepository) {
        this.policyRuleRepository = policyRuleRepository;
    }

    @Override
    public List<PolicyRule> getActiveRules() {
        return policyRuleRepository.findByActiveTrue();
    }
    // ... other standard CRUD methods
}