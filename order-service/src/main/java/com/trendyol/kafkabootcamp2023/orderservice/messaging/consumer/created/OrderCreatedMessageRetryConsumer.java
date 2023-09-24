package com.trendyol.kafkabootcamp2023.orderservice.messaging.consumer.created;

import com.trendyol.kafkabootcamp2023.orderservice.messaging.message.OrderCreatedMessage;
import com.trendyol.kafkabootcamp2023.orderservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "spring.kafka.consumer.order.created.retry.enabled", havingValue = "true")
public class OrderCreatedMessageRetryConsumer {

    private final Logger logger = LoggerFactory.getLogger(OrderCreatedMessageRetryConsumer.class);
    private final NotificationService notificationService;

    public OrderCreatedMessageRetryConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(
            topics = "${spring.kafka.consumer.order.created.retry.topic}",
            groupId = "${spring.kafka.consumer.order.created.retry.group-id}",
            containerFactory = "orderCreatedRetryKafkaListenerContainerFactory")
    public void consumeRetry(@Payload OrderCreatedMessage message,
                             @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
                             @Header(KafkaHeaders.OFFSET) Long offset,
                             @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        logger.info("{} RETRY message consumed with topic: {}, and partition: {}, and offset: {}, {}",
                message.getClass().getSimpleName(), topic, partition, offset, message);

        notificationService.sendNotification(message);
    }
}
