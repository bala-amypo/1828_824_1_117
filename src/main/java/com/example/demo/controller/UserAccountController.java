package com.example.demo.controller;
import com.example.demo.entity.UserAccount;
import com.example.demo.service.UserAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserAccountController {
    private final UserAccountService svc;
    public UserAccountController(UserAccountService svc) { this.svc = svc; }

    @PostMapping
    public ResponseEntity<UserAccount> create(@RequestBody UserAccount u) {
        return ResponseEntity.ok(svc.createUser(u));
    }
}