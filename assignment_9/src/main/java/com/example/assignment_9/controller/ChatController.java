package com.example.assignment_9.controller;

import com.example.assignment_9.dto.ChatRequest;
import com.example.assignment_9.dto.ChatResponse;
import com.example.assignment_9.service.DifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private DifyService difyService;

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        String answer = difyService.sendMessage(request.getMessage());
        return new ChatResponse(answer);
    }
}
