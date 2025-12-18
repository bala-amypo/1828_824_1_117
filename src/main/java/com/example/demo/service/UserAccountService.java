package com.example.demo.service;

import com.example.demo.entity.UserAccount;
import java.util.Optional;

public interface UserAccountService {
    UserAccount createUser(UserAccount user);
    Optional<UserAccount> getUserById(Long id);
    Optional<UserAccount> getUserByUsername(String username);
    Optional<UserAccount> getUserByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    UserAccount updateUser(Long id, UserAccount userDetails);
    void deleteUser(Long id);
    long countUsers();
}