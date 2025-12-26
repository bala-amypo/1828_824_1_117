package com.example.demo.repository;

import com.example.demo.entity.LoginEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoginEventRepository extends JpaRepository<LoginEvent, Long> {
    // Required for Priority 10
    List<LoginEvent> findByUserIdAndLoginStatus(Long userId, String loginStatus); 
    List<LoginEvent> findByUserId(Long userId);
}