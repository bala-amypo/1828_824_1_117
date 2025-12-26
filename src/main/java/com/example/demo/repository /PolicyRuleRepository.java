package com.example.demo.repository;
import com.example.demo.entity.PolicyRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PolicyRuleRepository extends JpaRepository<PolicyRule, Long> {
    List<PolicyRule> findByActiveTrue();
}