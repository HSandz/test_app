package com.example.producer_service.service;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.example.producer_service.model.Message;

@Service
public class KafkaProducerService {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    private static final String TOPIC = "messages";
    
    private final KafkaTemplate<String, Message> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Message> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(Message message) {
        CompletableFuture<SendResult<String, Message>> future = 
            kafkaTemplate.send(TOPIC, message.getId(), message);
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Sent message=[{}] with offset=[{}] to partition=[{}]", 
                    message, 
                    result.getRecordMetadata().offset(),
                    result.getRecordMetadata().partition());
            } else {
                logger.error("Unable to send message=[{}] due to : {}", message, ex.getMessage());
            }
        });
    }
}
