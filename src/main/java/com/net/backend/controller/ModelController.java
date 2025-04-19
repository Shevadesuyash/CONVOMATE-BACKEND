package com.net.backend.controller;

import com.net.backend.model.*;
import com.net.backend.service.EmailService;
import com.net.backend.service.ModelService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@RequestMapping("/model")
public class ModelController {

    private  EmailService emailService;


    private ModelService modelService;


    public ModelController(EmailService emailService,ModelService modelService) {
        this.emailService = emailService;
        this.modelService = modelService;
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


    @PostMapping("/translate")
    public ResponseEntity<?> translate(@RequestBody TranslationRequest request) {
        return modelService.translatePython(request);
    }

    @PostMapping("/correct_text")
    public ResponseEntity<?> grammarCheck(@RequestBody GrammarCheckRequest request) {
        return modelService.correctTextPython(request);
    }

    @PostMapping("/summarize")
    public ResponseEntity<?> summarize (@RequestBody SummarizeRequest request){
        return modelService.summarizeParagraph(request);
    }

    @PostMapping("/chat")
    public ResponseEntity<?> processChatMessage(@RequestBody ChatRequest request) {
        if (request.getMessage() == null || request.getMessage().isEmpty()) {
            return ResponseEntity.badRequest().body("Message cannot be empty");
        }
        return modelService.processMessage(request);
    }

    @GetMapping("/start")
    public ResponseEntity<?> startChat(){
        return modelService.startChat();
    }


}