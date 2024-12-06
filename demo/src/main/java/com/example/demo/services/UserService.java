package com.example.demo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        logger.info("UserService initialized");
    }

    /**
     * Проверяем, существует ли пользователь по email
     */
    public boolean existsByEmail(String email) {
        logger.info("Checking existence of user with email: {}", email);
        try {
            boolean exists = userRepository.existsByEmailNative(email);
            logger.info("User with email {} exists: {}", email, exists);
            return exists;
        } catch (Exception e) {
            logger.error("Error occurred while checking user by email: {}", email, e);
            throw e;
        }
    }

    /**
     * Создаем нового пользователя
     */
    public User createUser(User user) {
        logger.info("Creating user with email: {}", user.getEmail());
        try {
            User createdUser = userRepository.save(user);
            logger.info("User created successfully with ID: {}", createdUser.getId());
            return createdUser;
        } catch (Exception e) {
            logger.error("Error while creating user with email: {}", user.getEmail(), e);
            throw e;
        }
    }

    /**
     * Аутентификация пользователя по email и паролю
     */
    public User authenticateByEmail(String email, String password) {
        logger.info("Authenticating user with email: {}", email);
        try {
            User authenticatedUser = userRepository.authenticateByEmailNative(email, password);
            if (authenticatedUser != null) {
                logger.info("Authentication successful for user with email: {}", email);
            } else {
                logger.warn("Authentication failed for user with email: {}", email);
            }
            return authenticatedUser;
        } catch (Exception e) {
            logger.error("Error during authentication for email: {}", email, e);
            throw e;
        }
    }
}
