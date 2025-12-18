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
    
    // EXACT CONSTRUCTOR SIGNATURE AS REQUIRED
    public UserAccountServiceImpl(UserAccountRepository userAccountRepository, 
                                 PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public UserAccount createUser(UserAccount user) {
        // Check uniqueness
        if (userAccountRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (user.getEmail() != null && userAccountRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userAccountRepository.existsByEmployeeId(user.getEmployeeId())) {
            throw new IllegalArgumentException("Employee ID already exists");
        }
        
        // Set defaults
        if (user.getRole() == null) {
            user.setRole("USER");
        }
        if (user.getStatus() == null) {
            user.setStatus("ACTIVE");
        }
        
        // Hash password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        return userAccountRepository.save(user);
    }
    
    @Override
    public UserAccount getUserById(Long id) {
        return userAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    
    @Override
    public UserAccount updateUserStatus(Long id, String status) {
        UserAccount user = getUserById(id);
        user.setStatus(status);
        return userAccountRepository.save(user);
    }
    
    @Override
    public List<UserAccount> getAllUsers() {
        return userAccountRepository.findAll();
    }
    
    @Override
    public Optional<UserAccount> findByUsername(String username) {
        return userAccountRepository.findByUsername(username);
    }
}