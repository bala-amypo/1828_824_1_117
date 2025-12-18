package com.example.demo.service;

import com.example.demo.entity.UserAccount;
import java.util.List;

public interface UserAccountService {
    
    UserAccount createUser(UserAccount user); 

    UserAccount findByUsername(String username);
    UserAccount getUserById(Long id);
    UserAccount updateUserStatus(Long id, String status);
    List<UserAccount> getAllUsers();
}