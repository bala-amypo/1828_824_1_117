package com.example.demo.service;

import com.example.demo.entity.UserAccount;
import java.util.List;

public interface UserAccountService {
    UserAccount saveUser(UserAccount user);
    UserAccount findByUsername(String username);
    List<UserAccount> getAllUsers();
}