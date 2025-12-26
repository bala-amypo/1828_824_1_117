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

    /**
     * STEP 0.5 - Service Constructor Signatures:
     * LogicEventService must accept dependencies in this EXACT order:
     * 1. LoginEventRepository
     * 2. RuleEvaluationUtil
     */
    public LoginEventServiceImpl(LoginEventRepository loginEventRepository, RuleEvaluationUtil ruleEvaluationUtil) {
        this.loginEventRepository = loginEventRepository;
        this.ruleEvaluationUtil = ruleEvaluationUtil;
    }

    @Override
    public LoginEvent recordLogin(LoginEvent event) {
        // Save the event first to generate an ID
        LoginEvent savedEvent = loginEventRepository.save(event);
        
        // STEP 0.3 - Rule Evaluation:
        // Pass the event to the logic engine to check for policy violations
        ruleEvaluationUtil.evaluateLoginEvent(savedEvent);
        
        return savedEvent;
    }

    @Override
    public List<LoginEvent> getEventsByUser(Long userId) {
        // Uses the custom repository method signature from Step 1
        return loginEventRepository.findByUserId(userId);
    }

    @Override
    public List<LoginEvent> getSuspiciousLogins(Long userId) {
        // STEP 1 - Exact Naming Required: findByUserIdAndLoginStatus
        return loginEventRepository.findByUserIdAndLoginStatus(userId, "FAILED");
    }

    @Override
    public List<LoginEvent> getAllEvents() {
        return loginEventRepository.findAll();
    }
}