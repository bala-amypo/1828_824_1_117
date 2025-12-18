package com.example.demo.repository;

import com.example.demo.entity.PolicyRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PolicyRuleRepository extends JpaRepository<PolicyRule, Long> {
    List<PolicyRule> findByActiveTrue();
    Optional<PolicyRule> findByRuleCode(String ruleCode);
}