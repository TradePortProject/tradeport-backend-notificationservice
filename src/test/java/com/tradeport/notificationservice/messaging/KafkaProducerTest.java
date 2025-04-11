package com.tradeport.notificationservice.messaging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private KafkaProducer kafkaProducer;

    private final String testTopic = "test-topic";
    private final String testMessage = "Test Kafka Message";

    @BeforeEach
    void setUp() {
        kafkaProducer = new KafkaProducer(kafkaTemplate);
    }

    @Test
    void testSend_Success() {
        kafkaProducer.send(testTopic, testMessage);

        verify(kafkaTemplate, times(1)).send(testTopic, testMessage);
    }

    @Test
    void testSend_NullMessage() {
        kafkaProducer.send(testTopic, null);

        verify(kafkaTemplate, times(1)).send(testTopic, null);
    }

    @Test
    void testSend_EmptyMessage() {
        kafkaProducer.send(testTopic, "");

        verify(kafkaTemplate, times(1)).send(testTopic, "");
    }
}
