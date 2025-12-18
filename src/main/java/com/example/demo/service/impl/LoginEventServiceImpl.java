package com.example.demo.service.impl;

import com.example.demo.entity.LoginEvent;
import com.example.demo.repository.LoginEventRepository;
import com.example.demo.service.LoginEventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoginEventServiceImpl implements LoginEventService {

    private final LoginEventRepository loginEventRepository;

    public LoginEventServiceImpl(LoginEventRepository loginEventRepository) {
        this.loginEventRepository = loginEventRepository;
    }

    @Override
    @Transactional
    public LoginEvent recordLogin(LoginEvent event) {
        if (event.getEventTime() == null) {
            event.setEventTime(LocalDateTime.now());
        }
        return loginEventRepository.save(event);
    }

    @Override
    public List<LoginEvent> getAllEvents() {
        return loginEventRepository.findAll();
    }

    @Override
    public List<LoginEvent> getEventsByUser(Long userId) {
        return loginEventRepository.findByUserId(userId);
    }

    @Override
    public List<LoginEvent> getEventsByUser(String userId) {
        try {
            // Try to parse as Long first
            Long userIdLong = Long.parseLong(userId);
            return loginEventRepository.findByUserId(userIdLong);
        } catch (NumberFormatException e) {
            // If not a number, treat as username
            return loginEventRepository.findByUsername(userId);
        }
    }

    @Override
    public List<LoginEvent> getSuspiciousLogins(Long userId) {
        LocalDateTime lastHour = LocalDateTime.now().minusHours(1);
        return loginEventRepository.findSuspiciousLoginsByUserId(userId, lastHour);
    }

    @Override
    public List<LoginEvent> getSuspiciousLogins(String userId) {
        LocalDateTime lastHour = LocalDateTime.now().minusHours(1);
        try {
            // Try to parse as Long first
            Long userIdLong = Long.parseLong(userId);
            return loginEventRepository.findSuspiciousLoginsByUserId(userIdLong, lastHour);
        } catch (NumberFormatException e) {
            // If not a number, treat as username
            return loginEventRepository.findSuspiciousLoginsByUsername(userId, lastHour);
        }
    }

    @Override
    public LoginEvent getEventById(Long id) {
        return loginEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Login event not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteEvent(Long id) {
        loginEventRepository.deleteById(id);
    }
    
    // Additional helper methods
    public List<LoginEvent> getRecentEvents(Long userId, int hours) {
        LocalDateTime sinceTime = LocalDateTime.now().minusHours(hours);
        return loginEventRepository.findRecentEventsByUserId(userId, sinceTime);
    }
    
    public Long countFailedAttempts(Long userId, int minutes) {
        LocalDateTime sinceTime = LocalDateTime.now().minusMinutes(minutes);
        return loginEventRepository.countFailedAttemptsByUserId(userId, sinceTime);
    }
}