package com.example.assignment_8;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "Hello from Kubernetes Cloud Assignment!";
    }

    // Endpoint này giả lập tác vụ nặng để test Autoscaling (CPU Stress)
    @GetMapping("/stress")
    public String stress() {
        double x = 0.0001;
        for (int i = 0; i < 1000000; i++) {
            x += Math.sqrt(x);
        }
        return "CPU Stress Test Completed! Result: " + x;
    }
}
