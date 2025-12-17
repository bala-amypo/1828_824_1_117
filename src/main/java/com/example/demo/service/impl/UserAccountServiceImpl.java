package com.example.demo.service.impl;

import com.example.demo.entity.UserAccount;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.service.UserAccountService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userRepository;

    public UserAccountServiceImpl(UserAccountRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserAccount saveUser(UserAccount user) {
        return userRepository.save(user);
    }

    @Override
    public UserAccount findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<UserAccount> getAllUsers() {
        return userRepository.findAll();
    }
}