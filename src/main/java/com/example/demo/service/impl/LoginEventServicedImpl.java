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

    // Inject the utility as per technical constraints
    public LoginEventServiceImpl(LoginEventRepository loginEventRepository, RuleEvaluationUtil ruleEvaluationUtil) {
        this.loginEventRepository = loginEventRepository;
        this.ruleEvaluationUtil = ruleEvaluationUtil;
    }

    @Override
    public LoginEvent logEvent(LoginEvent event) {
        LoginEvent savedEvent = loginEventRepository.save(event);
        
        // Trigger the rule engine to check for violations immediately
        ruleEvaluationUtil.evaluateLoginEvent(savedEvent);
        
        return savedEvent;
    }

    @Override
    public List<LoginEvent> getAllEvents() {
        return loginEventRepository.findAll();
    }
}