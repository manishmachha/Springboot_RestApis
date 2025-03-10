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
    public ResponseEntity<Object> getUserById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        if (!userService.existsById(id)) {
            response.put("message", "User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else {
            User user = userService.getUserById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }

    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Update a user by ID
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id,@Valid @RequestBody User userDetails) {
        Map<String, Object> response = new HashMap<>();

        if (userService.existsByEmail(userDetails.getEmail())) {
            response.put("message", "User with this email already exists");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        if (userService.existsByUsername(userDetails.getUsername())) {
            response.put("message", "Username is already taken");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        User updatedUser = userService.updateUser(id, userDetails);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    // Delete a user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        if (!userService.existsById(id)) {
            response.put("message", "User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        response.clear();
        userService.deleteUser(id);
        if (!userService.existsById(id)) {
            response.put("message", " user " + id + " deleted successfully");
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
