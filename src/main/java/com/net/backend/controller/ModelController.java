package com.net.backend.controller;

import com.net.backend.model.EmailData;
import com.net.backend.model.TranslationRequest;
import com.net.backend.model.TranslationResponse;
import com.net.backend.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/model")
public class ModelController {

    private final EmailService emailService;
    private final String PYTHON_FLASK_URL = "http://127.0.0.1:5000/translate";

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



    @PostMapping("/translate")
    public ResponseEntity<?> translate(@RequestBody TranslationRequest request) {
        // Ensure the request payload is not null and contains the required fields

        log.info(request.toString());
        if (request.getTextToTranslate() == null || request.getTextToTranslate().isEmpty()) {
            return ResponseEntity.badRequest().body("No text provided for translation");
        }

        // Forward the request to the Python Flask backend
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the payload for the Python Flask backend
        Map<String, String> payload = new HashMap<>();
        payload.put("from_language", request.getFromLanguage());
        payload.put("to_language", request.getToLanguage());
        payload.put("text_to_translate", request.getTextToTranslate());

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(payload, headers);
        try {
            ResponseEntity<TranslationResponse> response = restTemplate.postForEntity(PYTHON_FLASK_URL, entity, TranslationResponse.class);
            log.info(response.toString());
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

}