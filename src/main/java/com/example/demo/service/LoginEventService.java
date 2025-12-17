package com.example.demo.service.impl;

import com.example.demo.entity.LoginEvent;
import com.example.demo.repository.LoginEventRepository;
import com.example.demo.service.LoginEventService;
import com.example.demo.util.RuleEvaluationUtil;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LoginEventServiceImpl implements LoginEventService {
    private final LoginEventRepository loginEventRepository;
    private final RuleEvaluationUtil ruleEvaluationUtil;

    public LoginEventServiceImpl(LoginEventRepository loginEventRepository, RuleEvaluationUtil ruleEvaluationUtil) {
        this.loginEventRepository = loginEventRepository;
        this.ruleEvaluationUtil = ruleEvaluationUtil;
    }

    @Override
    public LoginEvent recordLogin(LoginEvent event) {
        LoginEvent savedEvent = loginEventRepository.save(event);
        // Trigger violation detection logic (Constraint 3)
        ruleEvaluationUtil.evaluateLoginEvent(savedEvent);
        return savedEvent;
    }

    @Override
    public List<LoginEvent> getEventsByUser(Long userId) {
        return loginEventRepository.findByUseridAndLoginStatus(userId, "SUCCESS");
    }

    @Override
    public List<LoginEvent> getSuspiciousLogins(Long userId) {
        return loginEventRepository.findByUseridAndLoginStatus(userId, "FAILED");
    }

    @Override
    public List<LoginEvent> getAllEvents() {
        return loginEventRepository.findAll();
    }
}