package com.example.demo.service.impl;
import com.example.demo.entity.UserAccount;
import com.example.demo.Repository.UserAccountRepository;
import com.example.demo.service.userAccountService;
import org.springframework.security.crypto.password.passwordEncoder;
import org.springframework.stereotype.Service; //steereotype->automatic bean creation, define business logic, dependency injection
import java.util.List;

@service
