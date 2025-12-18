package com.example.demo.service;

import com.example.demo.entity.UserAccount;
import java.util.List;
import java.util.Optional;

public interface UserAccountService {
    UserAccount createUser(UserAccount user);
    UserAccount getUserById(Long id);
    UserAccount updateUserStatus(Long id, String status);
    List<UserAccount> getAllUsers();
    Optional<UserAccount> findByUsername(String username);
}