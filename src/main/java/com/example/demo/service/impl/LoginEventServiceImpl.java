@Service
public class LoginEventServiceImpl implements LoginEventService {
    private final LoginEventRepository loginRepo;
    private final RuleEvaluationUtil ruleEvaluator;

    // ORDER MATTERS: LoginEventRepository first, then RuleEvaluationUtil
    public LoginEventServiceImpl(LoginEventRepository loginRepo, RuleEvaluationUtil ruleEvaluator) {
        this.loginRepo = loginRepo;
        this.ruleEvaluator = ruleEvaluator;
    }

    @Override
    public LoginEvent recordLogin(LoginEvent event) {
        LoginEvent saved = loginRepo.save(event);
        ruleEvaluator.evaluateLoginEvent(saved); // Trigger logic engine
        return saved;
    }
    // ... other methods
}