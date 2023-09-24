package com.trendyol.kafkabootcamp2023.orderservice.messaging.consumer.delivered;

import com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer.retry.OrderCreatedMessageRetryProducer;
import com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer.retry.OrderDeliveredMessageRetryProducer;
import com.trendyol.kafkabootcamp2023.orderservice.messaging.message.OrderCreatedMessage;
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
@ConditionalOnProperty(value = "spring.kafka.consumer.order.delivered.enabled", havingValue = "true")
public class OrderDeliveredMessageConsumer {

    private final Logger logger = LoggerFactory.getLogger(OrderDeliveredMessageConsumer.class);
    private final OrderDeliveredMessageRetryProducer retryProducer;

    private final PaymentService paymentService;

    public OrderDeliveredMessageConsumer(OrderDeliveredMessageRetryProducer retryProducer,
                                         PaymentService paymentService) {
        this.retryProducer = retryProducer;
        this.paymentService = paymentService;
    }

    @KafkaListener(
            topics = "${spring.kafka.consumer.order.delivered.topic}",
            groupId = "${spring.kafka.consumer.order.delivered.group-id}",
            containerFactory = "orderDeliveredKafkaListenerContainerFactory")
    public void consume(@Payload OrderDeliveredMessage message,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
                        @Header(KafkaHeaders.OFFSET) Long offset,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        logger.info("{} message consumed with topic: {}, and partition: {}, and offset: {}, {}",
                message.getClass().getSimpleName(), topic, partition, offset, message);
        try {
            paymentService.createPayment(message);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            retryProducer.sendRetryMessage(message);
        }
    }
}
