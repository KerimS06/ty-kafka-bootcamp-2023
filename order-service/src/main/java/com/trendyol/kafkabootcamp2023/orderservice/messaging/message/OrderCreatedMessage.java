package com.trendyol.kafkabootcamp2023.orderservice.messaging.message;

import com.trendyol.kafkabootcamp2023.orderservice.domain.Order;
import com.trendyol.kafkabootcamp2023.orderservice.model.OrderType;

public class OrderCreatedMessage {

    private Long orderId;
    private Long customerId;
    private OrderType type;

    public OrderCreatedMessage() {
    }

    public OrderCreatedMessage(Order order) {
        this.orderId = order.getId();
        this.customerId = order.getCustomerId();
        this.type = order.getOrderType();
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "OrderCreatedMessage{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", type=" + type +
                '}';
    }
}
