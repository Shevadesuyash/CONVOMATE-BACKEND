package com.net.backend.controller;

import com.net.backend.entity.Review;
import com.net.backend.entity.User;
import com.net.backend.model.*;
import com.net.backend.service.OtpService;
import com.net.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
@Tag(name = "User API", description = "Endpoints for Managing Users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;


    // Register a new user
    @Operation(summary = "Register a new user", description = "Provide user details to register")
    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody UserData user) {
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

    @PostMapping("/generateOtp")
    public ResponseEntity<?> generateOtp(@RequestBody Email email) throws MessagingException, UnsupportedEncodingException {
        return userService.generateOtp(email.getEmail());
    }


    // Login user
    @Operation(summary = "Login a user", description = "Authenticate user with username and password")
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody EmailOtp emailOtp) {
        return otpService.verifyOtp(emailOtp.getEmail(), Integer.parseInt(emailOtp.getOtp()));

    }

    // Logout user
    @Operation(summary = "Logout a user", description = "Logout the user by username")
    @PostMapping("/logout")
    public String logoutUser(
            @RequestParam @Parameter(description = "Username of the user", required = true) String username) {

        log.info("User {} logged out and session invalidated.", username);

        return "User logged out successfully!";
    }

    @PostMapping("/review/submit")
    public ResponseEntity<Response> submitReview(@RequestBody ReviewForm reviewForm) {
        userService.submitReview(reviewForm);
        return new ResponseEntity<>(new Response("Review submitted successfully!"), HttpStatus.OK);
    }

    @GetMapping("/review/all")
    public ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.ok(userService.getAllReviews());
    }

}