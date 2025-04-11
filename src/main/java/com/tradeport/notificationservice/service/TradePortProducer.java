package com.tradeport.notificationservice.service;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.Future;

public class TradePortProducer {
    public static void main(String[] args) {
        // Define Kafka properties
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        // Create Kafka producer
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            // Send a message
            String topic = "tradeport-notify";
            String key = "messageKey";
            //String value = "{ \"NotificationID\": \"\",  \"UserID\": \"c56a4180-65aa-42ec-a945-5fd21dec0538\",  \"Subject\": \"TradePort Order - Your Order Update – Confirmed\",  \"Message\": \"Thank you for your order! We’ve received it and are processing it. You’ll receive another email once your order has shipped.\",  \"FromEmail\": \"prabhulalitha48@gmail.com\",  \"RecipientEmail\": \"prabhulalitha48@gmail.com\",  \"FailureReason\": null,  \"SentTime\": \"2025-04-06T23:07:59Z\",  \"EmailSend\": true,  \"CreatedOn\": \"2025-04-06T22:59:00Z\",  \"CreatedBy\": \"b8e6f9c2-3b24-4d7c-9eaf-85626d34b5a5\"}\n";
            String value = "{ \"NotificationID\": \"\",  \"UserID\": \"c56a4180-65aa-42ec-a945-5fd21dec0538\",  \"Subject\": \"TradePort Order - Your Order Update – Confirmed\",  \"Message\": \"Dear Prabhu,\\n\\nThank you for shopping with us! We’re excited to keep you updated on the status of your order.\\n\\nOrder Number: 45345345 \\n Placed On: 11/4/2025  \\n\\nWe’ll keep you updated as your order progresses.\\nIf you have any questions, feel free to contact our support team at tradeportprojecteam@nusu.onmicrosoft.com.\\n\\nThank you for choosing us!  \\nBest regards,  \\nTradePort Team\",  \"FromEmail\": \"prabhulalitha48@gmail.com\",  \"RecipientEmail\": \"e1509819@u.nus.edu\",  \"FailureReason\": null,  \"SentTime\": \"2025-04-06T23:07:59Z\",  \"EmailSend\": true,  \"CreatedOn\": \"2025-04-06T22:59:00Z\",  \"CreatedBy\": \"b8e6f9c2-3b24-4d7c-9eaf-85626d34b5a5\"}\n";

            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
            Future<RecordMetadata> future = producer.send(record);

            // Wait for the send operation to complete and log metadata
            RecordMetadata metadata = future.get();
            System.out.println("Message sent to topic: " + metadata.topic() +
                    ", partition: " + metadata.partition() +
                    ", offset: " + metadata.offset());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
