package com.tradeport.notificationservice.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradeport.notificationservice.messaging.strategy.MessagingStrategy;
import com.tradeport.notificationservice.model.NotificationTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NotificationMessageProcessorTest {

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private MessagingStrategy messagingStrategy;

    @InjectMocks
    private NotificationMessageProcessor notificationMessageProcessor;

    private final String testMessage = "{\"subject\":\"Test Subject\",\"message\":\"Test Message\",\"recipientEmail\":\"test@example.com\"}";

    @BeforeEach
    void setUp() {
        List<MessagingStrategy> strategies = Collections.singletonList(messagingStrategy);
        notificationMessageProcessor = new NotificationMessageProcessor(strategies, kafkaProducer);
    }

    @Test
    void testTransformData_ValidMessage() throws IOException {
        NotificationTO notificationTO = notificationMessageProcessor.transformData(testMessage);

        assertNotNull(notificationTO);
        assertEquals("Test Subject", notificationTO.getSubject());
        assertEquals("Test Message", notificationTO.getMessage());
        assertEquals("test@example.com", notificationTO.getRecipientEmail());
    }

    @Test
    void testTransformData_InvalidMessage() {
        String invalidMessage = "invalid-message";

        Exception exception = assertThrows(RuntimeException.class, () -> notificationMessageProcessor.transformData(invalidMessage));

        assertTrue(exception.getMessage().contains("Message transformation failed"));
    }

    @Test
    void testSendNotification() {
        NotificationTO notificationTO = new NotificationTO();
        notificationMessageProcessor.sendNotification(notificationTO);

        verify(messagingStrategy, times(1)).sendMessage(notificationTO);
    }

    @Test
    void testPostToTopic_Success() throws JsonProcessingException {
        NotificationTO notificationTO = new NotificationTO();
        notificationTO.setSubject("Test Post Subject");

        notificationMessageProcessor.postToTopic(notificationTO);

        verify(kafkaProducer, times(1)).send(anyString(), anyString());
    }
}
