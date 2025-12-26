package com.example.demo.config;

import com.example.demo.repository.PolicyRuleRepository;
import com.example.demo.repository.ViolationRecordRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.util.RuleEvaluationUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

    /**
     * Define JwtUtil as a Bean. 
     * The values match the requirements of the Master Test Suite.
     */
    @Bean
    public JwtUtil jwtUtil() {
        String secret = "TestSecretKeyForJWT1234567890";
        long validity = 3600000L; 
        return new JwtUtil(secret, validity, true);
    }

    /**
     * Define the PasswordEncoder used by UserAccountServiceImpl.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * FIX: Define RuleEvaluationUtil as a Bean.
     * Spring will automatically provide the ruleRepo and violationRepo 
     * from the JPA layer and inject them here.
     */
    @Bean
    public RuleEvaluationUtil ruleEvaluationUtil(
            PolicyRuleRepository ruleRepo, 
            ViolationRecordRepository violationRepo) {
        return new RuleEvaluationUtil(ruleRepo, violationRepo);
    }
}