package com.example.assignment_9.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class DifyService {

    @Value("${DIFY_API_KEY}")
    private String apiKey;

    @Value("${DIFY_BASE_URL}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String sendMessage(String message) {
        String url = baseUrl + "/chat-messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("inputs", new HashMap<>());
        body.put("query", message);
        body.put("response_mode", "blocking");
        body.put("user", "user-123"); // Static user ID for demo

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("answer")) {
                return (String) responseBody.get("answer");
            }
            return "Error: No answer from Dify";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
