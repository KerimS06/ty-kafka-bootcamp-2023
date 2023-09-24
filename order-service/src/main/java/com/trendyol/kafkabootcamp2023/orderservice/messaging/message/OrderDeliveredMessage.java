package com.trendyol.kafkabootcamp2023.orderservice.messaging.message;

import com.trendyol.kafkabootcamp2023.orderservice.domain.Order;
import com.trendyol.kafkabootcamp2023.orderservice.model.OrderType;

import java.math.BigDecimal;

public class OrderDeliveredMessage {
    private Long orderId;
    private Long sellerId;
    private BigDecimal amount;

    public OrderDeliveredMessage() {
    }

    public OrderDeliveredMessage(Order order) {
        this.orderId = order.getId();
        this.sellerId = order.getSellerId();
        this.amount = order.getOrderAmount();
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "OrderDeliveredMessage{" +
                "orderId=" + orderId +
                ", sellerId=" + sellerId +
                ", amount=" + amount +
                '}';
    }
}
