package com.trendyol.kafkabootcamp2023.orderservice.messaging.consumer.delivered;

import com.trendyol.kafkabootcamp2023.orderservice.messaging.message.OrderDeliveredMessage;
import com.trendyol.kafkabootcamp2023.orderservice.service.NotificationService;
import com.trendyol.kafkabootcamp2023.orderservice.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "spring.kafka.consumer.order.delivered.retry.enabled", havingValue = "true")
public class OrderDeliveredMessageRetryConsumer {

    private final Logger logger = LoggerFactory.getLogger(OrderDeliveredMessageRetryConsumer.class);
    private final PaymentService paymentService;

    public OrderDeliveredMessageRetryConsumer(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @KafkaListener(
            topics = "${spring.kafka.consumer.order.delivered.retry.topic}",
            groupId = "${spring.kafka.consumer.order.delivered.retry.group-id}",
            containerFactory = "orderDeliveredRetryKafkaListenerContainerFactory")
    public void consumeRetry(@Payload OrderDeliveredMessage message,
                             @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
                             @Header(KafkaHeaders.OFFSET) Long offset,
                             @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        logger.info("{} RETRY message consumed with topic: {}, and partition: {}, and offset: {}, {}",
                message.getClass().getSimpleName(), topic, partition, offset, message);

        paymentService.createPayment(message);
    }
}
