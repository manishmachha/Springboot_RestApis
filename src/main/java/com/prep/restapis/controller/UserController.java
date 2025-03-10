package com.prep.restapis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.prep.restapis.entity.User;
import com.prep.restapis.service.UserService;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        Map<String, Object> response = new HashMap<>();

        if (userService.existsByEmail(user.getEmail())) {
            response.put("message", "User with this email already exists");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        if (userService.existsByUsername(user.getUsername())) {
            response.put("message", "Username is already taken");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        User createdUser = userService.createUser(user);
        response.put("message", "User created successfully");
        response.put("user", createdUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Create a new user
    @PostMapping("/bulk")
    public ResponseEntity<Object> createUsers(@RequestBody List<User> users) {
        List<User> createdUsers = userService.createUsers(users);
        Map<String, List<User>> response = new HashMap<>();
        response.put("users", createdUsers);
        return new ResponseEntity<>(users, HttpStatus.CREATED);
    }

    // Get a user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Update a user by ID
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
