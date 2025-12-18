package com.example.demo.service.impl;

import com.example.demo.entity.UserAccount;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.service.UserAccountService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserAccountServiceImpl implements UserAccountService {
    
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserAccountServiceImpl(UserAccountRepository userAccountRepository, 
                                 PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public UserAccount createUser(UserAccount user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userAccountRepository.save(user);
    }
    
    @Override
    public Optional<UserAccount> getUserById(Long id) {
        return userAccountRepository.findById(id);
    }
    
    @Override
    public UserAccount getUserByUsername(String username) {
        return userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    @Override
    public List<UserAccount> getAllUsers() {
        return userAccountRepository.findAll();
    }
    
    @Override
    public UserAccount updateUserStatus(Long id, String status) {
        UserAccount user = userAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(status);
        return userAccountRepository.save(user);
    }
    
    @Override
    public void deleteUser(Long id) {
        userAccountRepository.deleteById(id);
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return userAccountRepository.existsByUsername(username);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return userAccountRepository.existsByEmail(email);
    }
}