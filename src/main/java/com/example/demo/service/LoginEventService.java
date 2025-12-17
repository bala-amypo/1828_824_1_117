package com.example.demo.service;

import com.example.demo.entity.LoginEvent;
import java.util.List;

public interface LoginEventService {
    LoginEvent logEvent(LoginEvent event);
    List<LoginEvent> getAllEvents();
}