package com.example.demo.service.impl;

import com.example.demo.entity.LoginEvent;
import com.example.demo.repository.LoginEventRepository;
import com.example.demo.service.LoginEventService;
import com.example.demo.util.RuleEvaluationUtil;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LoginEventServiceImpl implements LoginEventService {
    private final LoginEventRepository loginRepo;
    private final RuleEvaluationUtil ruleEvaluator;

    // Constructor MUST match the test: (loginRepo, ruleEvaluator)
    public LoginEventServiceImpl(LoginEventRepository loginRepo, RuleEvaluationUtil ruleEvaluator) {
        this.loginRepo = loginRepo;
        this.ruleEvaluator = ruleEvaluator;
    }

    @Override
    public LoginEvent recordLogin(LoginEvent event) {
        LoginEvent saved = loginRepo.save(event);
        ruleEvaluator.evaluateLoginEvent(saved); // Triggers Priorities 19-20
        return saved;
    }

    @Override
    public List<LoginEvent> getEventsByUser(Long userId) {
        return loginRepo.findByUserId(userId);
    }

    @Override
    public List<LoginEvent> getSuspiciousLogins(Long userId) {
        return loginRepo.findByUserIdAndLoginStatus(userId, "FAILED");
    }
}