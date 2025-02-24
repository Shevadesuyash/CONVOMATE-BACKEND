package com.net.backend.controller;

import com.net.backend.model.EmailData;
import com.net.backend.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/model")
public class ModelController {

    private final EmailService emailService;

    public ModelController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/create")
//    @PreAuthorize("hasRole('USER')") // Only users with the USER role can access this endpoint
    public String createModel() {
        return "Model created successfully";
    }

    @PostMapping("/sendMail")
//    @PreAuthorize("hasRole('ADMIN')") // Only users with the ADMIN role can access this endpoint
    public String sendMail(@RequestBody EmailData user) throws MessagingException, UnsupportedEncodingException {
        emailService.sendEmail(user);
        return "Success";
    }

    @GetMapping("/test")
    public String test() {
        return "Hello World!";
    }
}