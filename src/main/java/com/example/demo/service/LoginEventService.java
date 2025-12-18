package com.example.demo.service;

import com.example.demo.entity.LoginEvent;
import java.util.List;

public interface LoginEventService {
    LoginEvent recordLogin(LoginEvent event);
    List<LoginEvent> getAllEvents();
    List<LoginEvent> getEventsByUser(Long userId);
    List<LoginEvent> getEventsByUser(String userId); // Changed parameter type
    List<LoginEvent> getSuspiciousLogins(Long userId);
    List<LoginEvent> getSuspiciousLogins(String userId); // Changed parameter type
    LoginEvent getEventById(Long id);
    void deleteEvent(Long id);
}