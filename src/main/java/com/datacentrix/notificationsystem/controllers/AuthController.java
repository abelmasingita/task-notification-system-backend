package com.datacentrix.notificationsystem.controllers;

import com.datacentrix.notificationsystem.dto.LoginDto;
import com.datacentrix.notificationsystem.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser() {
        authService.logout();
        return ResponseEntity.noContent().build();
    }
}

