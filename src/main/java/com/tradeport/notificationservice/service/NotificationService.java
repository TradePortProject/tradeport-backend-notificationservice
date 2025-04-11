package com.tradeport.notificationservice.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.tradeport.notificationservice")
public class NotificationService {
    public static void main(String[] args) {
        SpringApplication.run(NotificationService.class, args);
        System.out.println("NotificationService is running on port 9098...");
    }
}
