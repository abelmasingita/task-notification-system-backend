package com.datacentrix.notificationsystem.controllers;

import com.datacentrix.notificationsystem.entity.User;
import com.datacentrix.notificationsystem.helper.UserMapper;
import com.datacentrix.notificationsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private UserMapper userMapper;


    // API endpoint to get a user by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(userMapper.toDtoOptional(user));
        } else {
            return ResponseEntity.status(404).body("User not found with ID: " + id);
        }
    }
}
