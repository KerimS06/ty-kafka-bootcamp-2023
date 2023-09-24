package com.trendyol.kafkabootcamp2023.orderservice.messaging.consumer.created;

import com.trendyol.kafkabootcamp2023.orderservice.messaging.message.OrderCreatedMessage;
import com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer.retry.OrderCreatedMessageRetryProducer;
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
@ConditionalOnProperty(value = "spring.kafka.consumer.order.created.enabled", havingValue = "true")
public class OrderCreatedMessageConsumer {

    private final Logger logger = LoggerFactory.getLogger(OrderCreatedMessageConsumer.class);
    private final OrderCreatedMessageRetryProducer retryProducer;
    private final NotificationService notificationService;

    public OrderCreatedMessageConsumer(OrderCreatedMessageRetryProducer retryProducer,
                                       NotificationService notificationService) {
        this.retryProducer = retryProducer;
        this.notificationService = notificationService;
    }

    @KafkaListener(
            topics = "${spring.kafka.consumer.order.created.topic}",
            groupId = "${spring.kafka.consumer.order.created.group-id}",
            containerFactory = "orderCreatedKafkaListenerContainerFactory")
    public void consume(@Payload OrderCreatedMessage message,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
                        @Header(KafkaHeaders.OFFSET) Long offset,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        logger.info("{} message consumed with topic: {}, and partition: {}, and offset: {}, {}",
                message.getClass().getSimpleName(), topic, partition, offset, message);
        try {
            notificationService.sendNotification(message);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            retryProducer.sendRetryMessage(message);
        }
    }
}
