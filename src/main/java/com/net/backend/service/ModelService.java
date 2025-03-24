package com.net.backend.service;

import com.net.backend.model.*;
import lombok.extern.slf4j.Slf4j;
import org.languagetool.JLanguageTool;
import org.languagetool.rules.RuleMatch;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ModelService {

    private final String PYTHON_FLASK_URL = "http://127.0.0.1:5000/translate";
    private final String pythonFlaskUrl = "http://127.0.0.1:5001/correct_text";

    public ResponseEntity<?> translatePython(TranslationRequest request) {
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

    public ResponseEntity<?> correctTextPython(GrammarCheckRequest request) {
        log.info("Received Grammar Check Request: {}", request);

        // Validate the input
        if (request.getParagraph() == null || request.getParagraph().isEmpty()) {
            return ResponseEntity.badRequest().body("No paragraph provided for grammar check");
        }

        // Forward the request to the Python Flask backend
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Payload for Python Flask grammar correction
        Map<String, String> payload = new HashMap<>();
        payload.put("paragraph", request.getParagraph()); // Use "text" as the key to match the Flask API

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(payload, headers);

        try {

            // Send the request to the Flask API
            ResponseEntity<GrammarCheckResponse> response = restTemplate.postForEntity(
                    pythonFlaskUrl,
                    entity,
                    GrammarCheckResponse.class
            );
            return ResponseEntity.ok(response.getBody());

        } catch (HttpClientErrorException e) {
            log.error("Grammar Check Error: {}", e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Internal Server Error during Grammar Check", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
}
