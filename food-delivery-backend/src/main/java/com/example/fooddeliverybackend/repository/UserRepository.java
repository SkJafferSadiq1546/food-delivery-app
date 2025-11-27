package com.example.fooddeliverybackend.repository;

import com.example.fooddeliverybackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    // This method allows us to find a user by their email address
    Optional<User> findByEmail(String email);
}