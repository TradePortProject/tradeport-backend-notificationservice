package com.tradeport.notificationservice.messaging;

import com.tradeport.notificationservice.model.NotificationTO;

public interface KafkaToMessageTemplate {
    void processMessage(String message);
    NotificationTO transformData(String message);
    void sendNotification(NotificationTO data);
    void postToTopic(NotificationTO message);
}
