package com.example.demo.service;

import com.example.demo.entity.UserAccount;
import java.util.List;
import java.util.Optional;

public interface UserAccountService {
    UserAccount createUser(UserAccount user);
    Optional<UserAccount> getUserById(Long id);
    UserAccount getUserByUsername(String username);
    List<UserAccount> getAllUsers();
    UserAccount updateUserStatus(Long id, String status);
    void deleteUser(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}