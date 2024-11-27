package com.example.demo.repositories;

import com.example.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // Method to check if a user with the given email exists
    boolean existsByEmail(String email);

    // Method to find a user by email
    User findByEmail(String email);
}
