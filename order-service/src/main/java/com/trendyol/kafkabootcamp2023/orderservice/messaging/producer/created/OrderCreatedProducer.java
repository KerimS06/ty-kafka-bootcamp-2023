package com.trendyol.kafkabootcamp2023.orderservice.messaging.producer.created;

import com.trendyol.kafkabootcamp2023.orderservice.messaging.producer.AbstractKafkaProducer;
import com.trendyol.kafkabootcamp2023.orderservice.messaging.message.OrderCreatedMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedProducer
        extends AbstractKafkaProducer<OrderCreatedMessage> {

    @Value("${spring.kafka.producer.order.created.topic}")
    private String topic;

    protected OrderCreatedProducer(KafkaTemplate<String, ?> kafkaTemplate) {
        super(kafkaTemplate);
    }

    @Override
    protected String partitionKey(OrderCreatedMessage message) {
        return message.getOrderId().toString();
    }

    public void produce(OrderCreatedMessage message) {
        super.produce(message, topic);
    }

}
