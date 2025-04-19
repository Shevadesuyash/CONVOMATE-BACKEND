package com.net.backend.service;

import com.net.backend.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ModelService {

    @Value("${python.flask.translate-url}")
    private String PYTHON_FLASK_URL;

    @Value("${python.flask.correct-text-url}")
    private String pythonFlaskUrl;

    @Value("${python.flask.summarizer-url}")
    private String pythonFlaskUrlSummarizer;

    @Value("${python.flask.chat-url}")
    private String pythonFlaskUrlChat;

    @Value("${python.flask.chat-start-url}")
    private String pythonFlaskUrlChatStart;

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
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
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
            log.error("Grammar Check Error: {}", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Internal Server Error during Grammar Check", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    public ResponseEntity<?> summarizeParagraph(SummarizeRequest request) {

        log.info("Received Paragraph Request: {}", request);

        // Validate the input
        if (request.getText() == null || request.getText().isEmpty()) {
            return ResponseEntity.badRequest().body("No paragraph provided for summarize");
        }

        // Forward the request to the Python Flask backend
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Payload for Python Flask grammar correction
        Map<String, String> payload = new HashMap<>();
        payload.put("text", request.getText());
        payload.put("type", request.getType());

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(payload, headers);

        try {

            // Send the request to the Flask API
            ResponseEntity<SummarizeResponse> response = restTemplate.postForEntity(
                    pythonFlaskUrlSummarizer,
                    entity,
                    SummarizeResponse.class
            );
            return ResponseEntity.ok(response.getBody());

        } catch (HttpClientErrorException e) {
            log.error("Summarizer Error: {}", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Internal Server Error during Grammar Check", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    public ResponseEntity<?> processMessage(ChatRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Payload for Python Flask chat API
        Map<String, String> payload = new HashMap<>();
        payload.put("message", request.getMessage());

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(payload, headers);

        try {
            // Call Python Flask API
            ResponseEntity<ChatResponse> response = restTemplate.postForEntity(
                    pythonFlaskUrlChat,
                    entity,
                    ChatResponse.class
            );
            log.info(response.toString());
            return ResponseEntity.ok(response.getBody());

        } catch (HttpClientErrorException e) {
            log.error("Chat API Error: {}", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Internal Server Error during chat processing", e);
            return ResponseEntity.internalServerError().body("Error processing chat message");
        }
    }

    public ResponseEntity<?> startChat() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // Optional for GET

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            // Call the Flask API directly without any query parameters
            ResponseEntity<ChatResponse> response = restTemplate.exchange(
                    pythonFlaskUrlChatStart,
                    HttpMethod.GET,
                    entity,
                    ChatResponse.class
            );

            return ResponseEntity.ok(response.getBody());

        } catch (HttpClientErrorException e) {
            log.error("Chat API Error: " + e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Internal Server Error during chat processing" + e.getMessage());
            return ResponseEntity.internalServerError().body("Error processing chat message");
        }
    }

    public void printurl() {
        log.info("PYTHON_FLASK_URL : "+PYTHON_FLASK_URL);
        log.info("pythonFlaskUrl : "+ pythonFlaskUrl);
        log.info("pythonFlaskUrlSummarizer : "+pythonFlaskUrlSummarizer);
        log.info("pythonFlaskUrlChat : "+ pythonFlaskUrlChat);
        log.info("pythonFlaskUrlChatStart : "+pythonFlaskUrlChatStart);
        log.info("working  :");
    }
}
