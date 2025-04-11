package com.tradeport.notificationservice.messaging.strategy;

import com.tradeport.notificationservice.model.NotificationTO;
import org.springframework.stereotype.Component;

public interface MessagingStrategy {
    void sendMessage(NotificationTO message);
}