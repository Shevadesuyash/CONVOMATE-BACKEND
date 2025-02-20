package com.net.backend.controller;

import com.net.backend.model.UserData;
import com.net.backend.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/model")
public class ModelController {

    private final EmailService emailService;

    public ModelController(EmailService emailService) {
        this.emailService = emailService;
    }


    @GetMapping("/create")
    public String createModel() {
        // Logic to create a new model
        return "Model created successfully";
    }

    @PostMapping("sendMail")
    public String sendMail(UserData user) throws MessagingException, UnsupportedEncodingException {

        emailService.register(user);
        return "Success";
    }
}

