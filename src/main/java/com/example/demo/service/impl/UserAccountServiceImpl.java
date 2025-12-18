package com.example.demo.service.impl;

import com.example.demo.entity.UserAccount;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.service.UserAccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAccountServiceImpl(UserAccountRepository userAccountRepository, 
                                 PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserAccount createUser(UserAccount user) {
        // Validate username uniqueness
        if (userAccountRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists: " + user.getUsername());
        }
        
        // Validate email uniqueness
        if (user.getEmail() != null && userAccountRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }
        
        // Encode password
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        // Set default values
        if (user.getAccountStatus() == null) {
            user.setAccountStatus("ACTIVE");
        }
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        
        return userAccountRepository.save(user);
    }

    @Override
    public Optional<UserAccount> getUserById(Long id) {
        return userAccountRepository.findById(id);
    }

    @Override
    public UserAccount getUserByUsername(String username) {
        return userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    @Override
    public List<UserAccount> getAllUsers() {
        return userAccountRepository.findAll();
    }

    @Override
    public Page<UserAccount> getAllUsers(Pageable pageable) {
        return userAccountRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public UserAccount updateUser(Long id, UserAccount userDetails) {
        return userAccountRepository.findById(id)
                .map(existingUser -> {
                    // Update only non-null fields
                    if (userDetails.getUsername() != null) {
                        // Check if new username is unique (if changed)
                        if (!existingUser.getUsername().equals(userDetails.getUsername()) &&
                            userAccountRepository.existsByUsername(userDetails.getUsername())) {
                            throw new RuntimeException("Username already exists: " + userDetails.getUsername());
                        }
                        existingUser.setUsername(userDetails.getUsername());
                    }
                    
                    if (userDetails.getEmail() != null) {
                        // Check if new email is unique (if changed)
                        if (!existingUser.getEmail().equals(userDetails.getEmail()) &&
                            userAccountRepository.existsByEmail(userDetails.getEmail())) {
                            throw new RuntimeException("Email already exists: " + userDetails.getEmail());
                        }
                        existingUser.setEmail(userDetails.getEmail());
                    }
                    
                    if (userDetails.getFullName() != null) {
                        existingUser.setFullName(userDetails.getFullName());
                    }
                    
                    if (userDetails.getRole() != null) {
                        existingUser.setRole(userDetails.getRole());
                    }
                    
                    if (userDetails.getAccountStatus() != null) {
                        existingUser.setAccountStatus(userDetails.getAccountStatus());
                    }
                    
                    if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                        existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
                    }
                    
                    existingUser.setUpdatedAt(LocalDateTime.now());
                    
                    return userAccountRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    @Transactional
    public UserAccount updateUserStatus(Long id, String status) {
        return userAccountRepository.findById(id)
                .map(user -> {
                    user.setAccountStatus(status);
                    user.setUpdatedAt(LocalDateTime.now());
                    return userAccountRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userAccountRepository.deleteById(id);
    }

    @Override
    public List<UserAccount> searchUsers(String keyword) {
        return userAccountRepository.searchUsers(keyword);
    }

    @Override
    public List<UserAccount> getUsersByRole(String role) {
        return userAccountRepository.findByRole(role);
    }

    @Override
    public List<UserAccount> getActiveUsers() {
        return userAccountRepository.findByAccountStatus("ACTIVE");
    }

    @Override
    public List<UserAccount> getInactiveUsers() {
        return userAccountRepository.findByAccountStatus("INACTIVE");
    }

    @Override
    public Long countUsers() {
        return userAccountRepository.count();
    }

    @Override
    public Long countActiveUsers() {
        return userAccountRepository.countByAccountStatus("ACTIVE");
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