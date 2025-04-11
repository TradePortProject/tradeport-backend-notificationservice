package com.tradeport.notificationservice.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    private final NotificationMessageProcessor notificationMessageProcessor;

    @Autowired
    public KafkaConsumer(NotificationMessageProcessor notificationMessageProcessor) {
        this.notificationMessageProcessor = notificationMessageProcessor;
        logger.info("KafkaConsumer initialized and ready to consume messages...");
    }

    //@KafkaListener(topics = "${kafka.topic.tradeport-notify}", groupId = "tradeport-group")
    @KafkaListener(topics = "tradeport-notify", groupId = "tradeport-group")
    public void consume(String message) {
        try {
            logger.info("Received Kafka message: {}", message);

            if (message == null || message.isEmpty()) {
                logger.warn("Received empty or null Kafka message. Skipping processing.");
                return;
            }

            notificationMessageProcessor.processMessage(message);
        } catch (SerializationException e) {
            logger.error("Error deserializing Kafka message: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error occurred while consuming message: {}", e.getMessage(), e); // More specific error message
        }
    }
}