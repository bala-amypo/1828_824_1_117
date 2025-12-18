package com.example.demo.repository;

import com.example.demo.entity.LoginEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoginEventRepository extends JpaRepository<LoginEvent, Long> {

    // Find by user ID (Long)
    List<LoginEvent> findByUserId(Long userId);
    
    // Find by username (String)
    @Query("SELECT le FROM LoginEvent le WHERE le.username = :userId")
    List<LoginEvent> findByUserIdString(@Param("userId") String userId);
    
    // Find suspicious logins for a user
    @Query("SELECT le FROM LoginEvent le WHERE (le.userId = :userId OR le.username = :userId) " +
           "AND le.eventTime >= :sinceTime " +
           "AND (le.suspicious = true OR le.status = 'FAILED') " +
           "ORDER BY le.eventTime DESC")
    List<LoginEvent> findSuspiciousLogins(@Param("userId") Object userId, 
                                          @Param("sinceTime") LocalDateTime sinceTime);
    
    // Find events with suspicious flag
    List<LoginEvent> findBySuspiciousTrue();
    
    // Find failed login attempts
    List<LoginEvent> findByStatus(String status);
    
    // Find events in time range
    List<LoginEvent> findByEventTimeBetween(LocalDateTime start, LocalDateTime end);
    
    
    List<LoginEvent> findByIpAddress(String ipAddress);
}