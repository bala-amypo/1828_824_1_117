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
    @Query("SELECT le FROM LoginEvent le WHERE le.username = :username")
    List<LoginEvent> findByUsername(@Param("username") String username);
    
    // Find suspicious logins for a user by user ID (Long)
    @Query("SELECT le FROM LoginEvent le WHERE le.userId = :userId " +
           "AND le.eventTime >= :sinceTime " +
           "AND (le.suspicious = true OR le.status = 'FAILED') " +
           "ORDER BY le.eventTime DESC")
    List<LoginEvent> findSuspiciousLoginsByUserId(@Param("userId") Long userId, 
                                                 @Param("sinceTime") LocalDateTime sinceTime);
    
    // Find suspicious logins for a user by username (String)
    @Query("SELECT le FROM LoginEvent le WHERE le.username = :username " +
           "AND le.eventTime >= :sinceTime " +
           "AND (le.suspicious = true OR le.status = 'FAILED') " +
           "ORDER BY le.eventTime DESC")
    List<LoginEvent> findSuspiciousLoginsByUsername(@Param("username") String username, 
                                                   @Param("sinceTime") LocalDateTime sinceTime);
    
    // Find events with suspicious flag
    List<LoginEvent> findBySuspiciousTrue();
    
    // Find failed login attempts
    List<LoginEvent> findByStatus(String status);
    
    // Find events in time range
    List<LoginEvent> findByEventTimeBetween(LocalDateTime start, LocalDateTime end);
    
    // Find by IP address
    List<LoginEvent> findByIpAddress(String ipAddress);
    
    // Find recent events for a user
    @Query("SELECT le FROM LoginEvent le WHERE le.userId = :userId " +
           "AND le.eventTime >= :sinceTime " +
           "ORDER BY le.eventTime DESC")
    List<LoginEvent> findRecentEventsByUserId(@Param("userId") Long userId, 
                                             @Param("sinceTime") LocalDateTime sinceTime);
    
    // Find recent events for a user by username
    @Query("SELECT le FROM LoginEvent le WHERE le.username = :username " +
           "AND le.eventTime >= :sinceTime " +
           "ORDER BY le.eventTime DESC")
    List<LoginEvent> findRecentEventsByUsername(@Param("username") String username, 
                                               @Param("sinceTime") LocalDateTime sinceTime);
    
    // Count failed attempts for a user
    @Query("SELECT COUNT(le) FROM LoginEvent le WHERE le.userId = :userId " +
           "AND le.status = 'FAILED' " +
           "AND le.eventTime >= :sinceTime")
    Long countFailedAttemptsByUserId(@Param("userId") Long userId, 
                                    @Param("sinceTime") LocalDateTime sinceTime);
}