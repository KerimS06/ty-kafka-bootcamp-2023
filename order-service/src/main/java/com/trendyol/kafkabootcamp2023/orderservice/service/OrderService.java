package com.trendyol.kafkabootcamp2023.orderservice.service;

import com.trendyol.kafkabootcamp2023.orderservice.domain.Order;
import com.trendyol.kafkabootcamp2023.orderservice.messaging.message.OrderCreatedMessage;
import com.trendyol.kafkabootcamp2023.orderservice.model.request.OrderCreateRequest;
import com.trendyol.kafkabootcamp2023.orderservice.model.request.OrderStatusUpdateRequest;
import com.trendyol.kafkabootcamp2023.orderservice.model.response.OrderResponse;
import com.trendyol.kafkabootcamp2023.orderservice.messaging.producer.created.OrderCreatedProducer;
import com.trendyol.kafkabootcamp2023.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final OrderRepository orderRepository;
    private final OrderCreatedProducer orderCreatedProducer;

    public OrderService(OrderRepository orderRepository, OrderCreatedProducer orderCreatedProducer) {
        this.orderRepository = orderRepository;
        this.orderCreatedProducer = orderCreatedProducer;
    }

    public OrderResponse createOrder(OrderCreateRequest orderCreateRequest) {
        Order order = orderRepository.save(orderCreateRequest.toOrder());

        orderCreatedProducer.produce(new OrderCreatedMessage(order));
        logger.info("OrderCreated with: {}", orderCreateRequest);

        return new OrderResponse(order);
    }


    public OrderResponse updateOrderStatus(OrderStatusUpdateRequest request) {
       //TODO:: update order status and if status is delivered produce orderDeliveredEvent.
       //TODO:: Afterwards create payment by calling PaymentService after consuming orderDeliveredEvent
        return null;
    }
}
