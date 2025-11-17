package com.example.consumer_service.service;

import com.example.consumer_service.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "messages", groupId = "message-consumer-group")
    public void consume(
        @Payload Message message,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
        @Header(KafkaHeaders.OFFSET) long offset) {
        
        logger.info("Received message: {} from partition: {} with offset: {}", 
            message, partition, offset);
        
        // Process the message here
        processMessage(message);
    }

    private void processMessage(Message message) {
        logger.info("Processing message with ID: {} and content: {}", 
            message.getId(), message.getContent());
        // Add your business logic here
    }
}
