package com.example.demo.repository;

import com.example.demo.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findByUsername(String username);
    
    Optional<UserAccount> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    List<UserAccount> findByRole(String role);
    
    List<UserAccount> findByAccountStatus(String accountStatus);
    
    Long countByAccountStatus(String accountStatus);
    
    @Query("SELECT u FROM UserAccount u WHERE " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<UserAccount> searchUsers(@Param("keyword") String keyword);
    
    @Query("SELECT u FROM UserAccount u WHERE u.accountStatus = 'ACTIVE' ORDER BY u.createdAt DESC")
    List<UserAccount> findActiveUsers();
    
    List<UserAccount> findByFailedLoginAttemptsGreaterThan(Integer attempts);
}