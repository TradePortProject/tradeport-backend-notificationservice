package com.tradeport.notificationservice.messaging.strategy;

import com.tradeport.notificationservice.model.NotificationTO;
import org.springframework.stereotype.Component;

@Component

public class SmsStrategy implements MessagingStrategy {
    @Override
    public void sendMessage(NotificationTO message) {
        System.out.println("Sending SMS with message - not implemented : " + message);
    }
}