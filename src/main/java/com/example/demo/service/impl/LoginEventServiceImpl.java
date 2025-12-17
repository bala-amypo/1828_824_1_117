package com.example.demo.service.impl;

import com.example.demo.entity.LoginEvent;
import com.example.demo.repository.LoginEventRepository;
import com.example.demo.service.LoginEventService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LoginEventServiceImpl implements LoginEventService {
    private final LoginEventRepository repository;
    public LoginEventServiceImpl(LoginEventRepository repository) { this.repository = repository; }

    @Override
    public LoginEvent logEvent(LoginEvent event) { return repository.save(event); }
    @Override
    public List<LoginEvent> getAllEvents() { return repository.findAll(); }
}