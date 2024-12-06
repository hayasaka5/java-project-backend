package com.example.demo.controllers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.entities.User;
import com.example.demo.services.UserService;
import com.example.demo.utils.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Регулярное выражение для проверки корректности email
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern emailPattern = Pattern.compile(EMAIL_REGEX);

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        // Проверка корректности email
        if (!isValidEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Invalid email format.");
        }

        // Проверка пароля
        if (!PasswordValidator.isValidPassword(user.getPassword())) {
            return ResponseEntity.badRequest().body("Password must contain at least 8 characters, including uppercase, lowercase, a number, and a special character.");
        }

        // Проверка существования пользователя с таким email
        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already taken.");
        }

        // Создание пользователя
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        User loggedInUser = userService.authenticateByEmail(loginRequest.getEmail(), loginRequest.getPassword());

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        return ResponseEntity.ok(loggedInUser);
    }

    // Метод для проверки формата email
    private boolean isValidEmail(String email) {
        return email != null && emailPattern.matcher(email).matches();
    }
}
