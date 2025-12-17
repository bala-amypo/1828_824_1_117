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
    public UserAccount createUser(UserAccount user) {
        return userRepository.save(user);
    }

    @Override
    public UserAccount findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserAccount getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public UserAccount updateUserStatus(Long id, String status) {
        UserAccount user = getUserById(id);
        if (user != null) {
            user.setRole(status); // Or a specific status field if you have one
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public List<UserAccount> getAllUsers() {
        return userRepository.findAll();
    }
}