package com.example.demo.service.impl;

import com.example.demo.entity.LoginEvent;
import com.example.demo.repository.LoginEventRepository;
import com.example.demo.service.LoginEventService;
import com.example.demo.util.RuleEvaluationUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class LoginEventServiceImpl implements LoginEventService {
    
    private final LoginEventRepository loginEventRepository;
    private final RuleEvaluationUtil ruleEvaluationUtil;
    
    // EXACT CONSTRUCTOR SIGNATURE AS REQUIRED
    public LoginEventServiceImpl(LoginEventRepository loginEventRepository,
                                RuleEvaluationUtil ruleEvaluationUtil) {
        this.loginEventRepository = loginEventRepository;
        this.ruleEvaluationUtil = ruleEvaluationUtil;
    }
    
    @Override
    public LoginEvent recordLogin(LoginEvent event) {
        // Validate required fields
        if (event.getIpAddress() == null || event.getIpAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("IP address cannot be null");
        }
        if (event.getDeviceId() == null || event.getDeviceId().trim().isEmpty()) {
            throw new IllegalArgumentException("Device ID cannot be null");
        }
        
        // Save the event
        LoginEvent savedEvent = loginEventRepository.save(event);
        
        // Evaluate rules
        ruleEvaluationUtil.evaluateLoginEvent(savedEvent);
        
        return savedEvent;
    }
    
    @Override
    public List<LoginEvent> getEventsByUser(Long userId) {
        return loginEventRepository.findByUserId(userId);
    }
    
    @Override
    public List<LoginEvent> getSuspiciousLogins(Long userId) {
        return loginEventRepository.findByUserIdAndLoginStatus(userId, "FAILED");
    }
    
    @Override
    public List<LoginEvent> getAllEvents() {
        return loginEventRepository.findAll();
    }
}