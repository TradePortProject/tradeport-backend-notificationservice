package com.tradeport.notificationservice.messaging.strategy;

import com.tradeport.notificationservice.model.NotificationTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.mail.MessagingException;
import javax.mail.Transport;
import java.util.Date;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailStrategyTest {

    @InjectMocks
    private EmailStrategy emailStrategy;

    @Mock
    private Transport transport; // Mock Transport to prevent real email sending

    private NotificationTO notification;

    @BeforeEach
    void setUp() {
        notification = new NotificationTO();
        notification.setSubject("Test Subject");
        notification.setMessage("Test Message");
        notification.setCreatedOn(new Date());
        notification.setRecipientEmail("test@example.com");
    }

    @Test
    void testSendMessage_Success() throws Exception {
        // Call the method under test
        emailStrategy.sendMessage(notification);

        // Verify the email properties were correctly set
        verify(transport, times(1)).send(any()); // Ensure Transport.send() was called once

        // Ensure email metadata is correctly updated
        assert notification.isEmailSend();
        assert notification.getSentTime() != null;
        assert notification.getRecipientEmail().equals("test@example.com");
    }

    @Test
    void testSendMessage_Failure() throws MessagingException {
        // Simulate a failure scenario
        doThrow(new RuntimeException("SMTP error")).when(transport).send(any());

        emailStrategy.sendMessage(notification);

        // Ensure failure scenario gets logged
        assert notification.getRecipientEmail().equals("SMTP error");
    }
}
