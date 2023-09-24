package com.trendyol.kafkabootcamp2023.orderservice.model.response;

import com.trendyol.kafkabootcamp2023.orderservice.domain.Order;
import com.trendyol.kafkabootcamp2023.orderservice.model.OrderStatus;
import com.trendyol.kafkabootcamp2023.orderservice.model.OrderType;

import java.math.BigDecimal;

public class OrderResponse {
    private Long id;
    private Long sellerId;
    private Long customerId;
    private OrderStatus orderStatus;
    private OrderType orderType;
    private BigDecimal orderAmount;

    public OrderResponse() {
    }

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.sellerId = order.getSellerId();
        this.customerId = order.getCustomerId();
        this.orderStatus = order.getOrderStatus();
        this.orderType = order.getOrderType();
        this.orderAmount = order.getOrderAmount();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }
}
