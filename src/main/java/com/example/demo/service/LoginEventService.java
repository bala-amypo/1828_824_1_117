package com.example.demo.service;

import com.example.demo.entity.LoginEvent;
import com.example.demo.dto.LoginEventRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

public interface LoginEventService {
    LoginEvent logEvent(LoginEventRequest request);
    List<LoginEvent> getAllEvents();
    Page<LoginEvent> getEvents(LocalDateTime startDate, LocalDateTime endDate, String userId, String ipAddress, String status, Pageable pageable);
    LoginEvent getEventById(Long id);
    void deleteEvent(Long id);
    long countEventsByStatus(String status);
    List<LoginEvent> getFailedAttempts(String userId, LocalDateTime startTime);
}