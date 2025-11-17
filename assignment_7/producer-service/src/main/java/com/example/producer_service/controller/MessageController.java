package com.example.producer_service.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.producer_service.model.Message;
import com.example.producer_service.service.KafkaProducerService;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final KafkaProducerService producerService;

    public MessageController(KafkaProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestBody MessageRequest request) {
        Message message = new Message(
            UUID.randomUUID().toString(),
            request.getContent(),
            LocalDateTime.now(),
            "producer-service"
        );
        
        producerService.sendMessage(message);
        return ResponseEntity.ok("Message sent: " + message.getId());
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Producer service is running");
    }
}

class MessageRequest {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
