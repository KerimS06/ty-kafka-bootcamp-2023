package com.trendyol.kafkabootcamp2023.orderservice.messaging.producer.updated;

import com.trendyol.kafkabootcamp2023.orderservice.messaging.message.OrderCreatedMessage;
import com.trendyol.kafkabootcamp2023.orderservice.messaging.message.OrderDeliveredMessage;
import com.trendyol.kafkabootcamp2023.orderservice.messaging.producer.AbstractKafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

public class OrderDeliveredProducer extends AbstractKafkaProducer<OrderDeliveredMessage> {

    @Value("${spring.kafka.producer.order.delivered.topic}")
    private String topic;

    protected OrderDeliveredProducer(KafkaTemplate<String, ?> kafkaTemplate) {
        super(kafkaTemplate);
    }

    public void produce(OrderDeliveredMessage message) {
        super.produce(message, topic);
    }

    @Override
    protected String partitionKey(OrderDeliveredMessage message) {
        return null;
    }
}
