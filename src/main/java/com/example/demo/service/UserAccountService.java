package com.example.demo.service;

import com.example.demo.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserAccountService {
    UserAccount createUser(UserAccount user);
    Optional<UserAccount> getUserById(Long id);
    UserAccount getUserByUsername(String username);
    List<UserAccount> getAllUsers();
    Page<UserAccount> getAllUsers(Pageable pageable);
    UserAccount updateUser(Long id, UserAccount userDetails);
    UserAccount updateUserStatus(Long id, String status);
    void deleteUser(Long id);
    List<UserAccount> searchUsers(String keyword);
    List<UserAccount> getUsersByRole(String role);
    List<UserAccount> getActiveUsers();
    List<UserAccount> getInactiveUsers();
    Long countUsers();
    Long countActiveUsers();
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}