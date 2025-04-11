package com.tradeport.notificationservice.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradeport.notificationservice.model.NotificationTO;
import com.tradeport.notificationservice.messaging.strategy.MessagingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class NotificationMessageProcessor implements KafkaToMessageTemplate {
    private static final Logger logger = LoggerFactory.getLogger(NotificationMessageProcessor.class);

    private final List<MessagingStrategy> messagingStrategies;
    private final KafkaProducer kafkaProducer;

    @Value("${kafka.topic.notified:tradeport-notified}") // Use @Value for topicName
    private String topicName;

    @Autowired
    public NotificationMessageProcessor(List<MessagingStrategy> messagingStrategies, KafkaProducer kafkaProducer) {
        this.messagingStrategies = messagingStrategies;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void processMessage(String message) {
        logger.info("Processing Kafka message...");
        NotificationTO processedData = transformData(message);
        sendNotification(processedData);
        postToTopic(processedData);
    }

    @Override
    public NotificationTO transformData(String message) {
        logger.info("Transforming message: {}", message);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(message, NotificationTO.class);
        } catch (IOException e) {
            logger.error("Failed to transform message: {}", e.getMessage(), e);
            throw new RuntimeException("Message transformation failed", e);
        }
    }

    @Override
    public void sendNotification(NotificationTO data) {
        logger.info("Notifying users with data: {}", data);
        for (MessagingStrategy strategy : messagingStrategies) {
            strategy.sendMessage(data);
        }
    }

    @Override
    public void postToTopic(NotificationTO notificationTO) {
        logger.info("Posting message to Kafka topic: {}", topicName); // Log the topic name
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonMessage = objectMapper.writeValueAsString(notificationTO);
            kafkaProducer.send(topicName, jsonMessage);
        } catch (JsonProcessingException e) {
            logger.error("Error posting message to topic {}: {}", topicName, e.getMessage(), e); // Include topic name in error
        }
    }
}