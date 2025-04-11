package com.tradeport.notificationservice.messaging;

import com.tradeport.notificationservice.messaging.KafkaToMessageTemplate;
import org.apache.kafka.common.errors.SerializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerTest {

    @InjectMocks
    private KafkaConsumer kafkaConsumer;

    @Mock
    private KafkaToMessageTemplate kafkaToMessageTemplate;

    @BeforeEach
    void setUp() {
        // Setup any required initialization here if needed
    }

    @Test
    void testConsume_ValidMessage() {
        String validMessage = "Test Kafka Message";

        kafkaConsumer.consume(validMessage);

        verify(kafkaToMessageTemplate, times(1)).processMessage(validMessage);
    }

    @Test
    void testConsume_NullMessage() {
        kafkaConsumer.consume(null);

        verify(kafkaToMessageTemplate, never()).processMessage(anyString());
    }

    @Test
    void testConsume_EmptyMessage() {
        kafkaConsumer.consume("");

        verify(kafkaToMessageTemplate, never()).processMessage(anyString());
    }

    @Test
    void testConsume_SerializationExceptionHandling() {
        String faultyMessage = "Faulty Message";

        doThrow(new SerializationException("Serialization error")).when(kafkaToMessageTemplate).processMessage(faultyMessage);

        Exception exception = assertThrows(SerializationException.class, () -> kafkaConsumer.consume(faultyMessage));

        assertEquals("Serialization error", exception.getMessage());
    }

    @Test
    void testConsume_UnexpectedExceptionHandling() {
        String message = "Unexpected error message";

        doThrow(new RuntimeException("Unexpected exception")).when(kafkaToMessageTemplate).processMessage(message);

        Exception exception = assertThrows(RuntimeException.class, () -> kafkaConsumer.consume(message));

        assertEquals("Unexpected exception", exception.getMessage());
    }
}
