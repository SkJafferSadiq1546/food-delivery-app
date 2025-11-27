package com.example.fooddeliverybackend.controller;

import com.example.fooddeliverybackend.model.User;
import com.example.fooddeliverybackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Endpoint for user login
    @PostMapping("/auth/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginDetails) {
        Optional<User> optionalUser = userRepository.findByEmail(loginDetails.getEmail());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getPassword().equals(loginDetails.getPassword())) {
                return ResponseEntity.ok(user); // Success: return the full user object
            }
        }
        // Failure: return an unauthorized error
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }

    // Endpoint for new user registration
    @PostMapping("/auth/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    // Endpoint for deleting a user account
    @DeleteMapping("/user/profile/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok("User account deleted successfully.");
    }
}