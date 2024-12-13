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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern emailPattern = Pattern.compile(EMAIL_REGEX);

    @Operation(
            summary = "Create a new user",
            description = "Creates a new user after validating email format and password security. Ensures no existing user has the provided email.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid email format or password not meeting security requirements"),
                    @ApiResponse(responseCode = "401", description = "Email already taken"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (!isValidEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Invalid email format.");
        }

        if (!PasswordValidator.isValidPassword(user.getPassword())) {
            return ResponseEntity.badRequest().body("Password must contain at least 8 characters, including uppercase, lowercase, a number, and a special character.");
        }

        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already taken.");
        }

        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @Operation(
            summary = "Login user",
            description = "Authenticates a user by their email and password. Returns a 401 status if credentials are invalid.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid email or password"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        User loggedInUser = userService.authenticateByEmail(loginRequest.getEmail(), loginRequest.getPassword());

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        return ResponseEntity.ok(loggedInUser);
    }

    private boolean isValidEmail(String email) {
        return email != null && emailPattern.matcher(email).matches();
    }
}
