package com.trendyol.kafkabootcamp2023.orderservice.service;

import com.trendyol.kafkabootcamp2023.orderservice.messaging.message.OrderCreatedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public void sendNotification(OrderCreatedMessage message) {
        logger.info("Notification is sent for customer id: {}, message: {}", message.getCustomerId(), message);
    }



}
