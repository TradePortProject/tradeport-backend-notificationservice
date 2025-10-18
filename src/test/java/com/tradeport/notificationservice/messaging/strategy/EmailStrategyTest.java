package com.tradeport.notificationservice.messaging.strategy;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.tradeport.notificationservice.model.NotificationTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EmailStrategyTest {

    @Mock
    private AmazonSimpleEmailService sesClient;

    @InjectMocks
    private EmailStrategy emailStrategy;

    private NotificationTO notification;

    @BeforeEach
    void setUp() {
        // Initialize notification object with test data
        notification = new NotificationTO();
        notification.setRecipientEmail("test@example.com");
        notification.setSubject("Test Subject");
        notification.setMessage("Test email message");

        // Inject values into EmailStrategy
        ReflectionTestUtils.setField(emailStrategy, "fromEmail", "noreply@tradeport.com");
    }

    @Test
    void testSendMessage_Success() {
        // Mock SES email sending
        doNothing().when(sesClient).sendEmail(any(SendEmailRequest.class));

        // Execute method
        emailStrategy.sendMessage(notification);

        // Assertions
        assertEquals("noreply@tradeport.com", notification.getFromEmail());
        assertNotNull(notification.getSentTime());
        assertTrue(notification.isEmailSend());
    }

    @Test
    void testSendMessage_Failure() {
        // Mock SES failure scenario
        doThrow(new RuntimeException("SES Error")).when(sesClient).sendEmail(any(SendEmailRequest.class));

        // Execute method
        emailStrategy.sendMessage(notification);

        // Assertions
        assertEquals("test@example.com", notification.getRecipientEmail()); // Ensures recipient is still set
        assertFalse(notification.isEmailSend()); // Should be false due to failure
    }
}
