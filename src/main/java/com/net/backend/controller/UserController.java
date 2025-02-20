package com.net.backend.controller;

import com.net.backend.entity.User;
import com.net.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
@Tag(name = "User API", description = "Endpoints for Managing Users")
public class UserController {

    @Autowired
    private UserService userService;


    // Register a new user
    @Operation(summary = "Register a new user", description = "Provide user details to register")
    @PostMapping("/register")
    public User registerUser(@RequestBody @Parameter(description = "User object to be created", required = true) User user) {
        log.info("Register with user {}", user);
        return userService.registerUser(user);
    }

    // Fetch user profile by username
    @Operation(summary = "Get user profile", description = "Fetch user profile by username")
    @GetMapping("/profile")
    public User getUserProfile(
            @RequestParam @Parameter(description = "Username of the user", required = true) String username) {
        return userService.getUserProfile(username);
    }

    // Login user and store the session in HttpSession
    @Operation(summary = "Login a user", description = "Authenticate user with username and password")
    @PostMapping("/login")
    public User loginUser(
            @RequestParam @Parameter(description = "Username of the user", required = true) String username,
            @RequestParam @Parameter(description = "Password of the user", required = true) String password,
            HttpSession session) {
        User user = userService.loginUser(username, password);

        if (user != null) {
            // Store user session data
            session.setAttribute("username", username);
            log.info("User {} logged in successfully. Session created.", username);
        }

        return user;
    }

    // Logout user and invalidate session
    @Operation(summary = "Logout a user", description = "Logout the user by username")
    @PostMapping("/logout")
    public String logoutUser(
            @RequestParam @Parameter(description = "Username of the user", required = true) String username,
            HttpSession session) {

        // Invalidate session
        session.invalidate();
        log.info("User {} logged out and session invalidated.", username);

        return "User logged out successfully!";
    }

    // Check session to verify if a user is logged in
    @Operation(summary = "Check session", description = "Verify if the user is logged in based on the session")
    @GetMapping("/check-session")
    public String checkSession(HttpSession session) {
        String username = (String) session.getAttribute("username");
        return username != null ? "User " + username + " is logged in." : "No active session found.";
    }
}
