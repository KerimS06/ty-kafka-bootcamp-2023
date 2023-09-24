package com.trendyol.kafkabootcamp2023.orderservice.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trendyol.kafkabootcamp2023.orderservice.domain.Order;
import com.trendyol.kafkabootcamp2023.orderservice.domain.builder.OrderBuilder;
import com.trendyol.kafkabootcamp2023.orderservice.model.OrderStatus;
import com.trendyol.kafkabootcamp2023.orderservice.model.OrderType;
import com.trendyol.kafkabootcamp2023.orderservice.util.Clock;

import java.math.BigDecimal;
import java.util.Date;

public class OrderCreateRequest {

    @JsonIgnore
    private final OrderStatus orderStatus = OrderStatus.CREATED;

    private Long sellerId;
    private Long customerId;
    private OrderType orderType;
    private BigDecimal orderAmount;

    private Date orderDate;


    public OrderStatus getOrderStatus() {
        return orderStatus;
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

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "OrderCreateRequest{" +
                "orderStatus=" + orderStatus +
                ", sellerId=" + sellerId +
                ", customerId=" + customerId +
                ", orderType=" + orderType +
                ", orderAmount=" + orderAmount +
                '}';
    }

    public Order toOrder() {
        return OrderBuilder.anOrder()
                .orderDate(Clock.now().toDate())
                .sellerId(this.sellerId)
                .customerId(this.customerId)
                .orderType(this.orderType)
                .orderStatus(this.orderStatus)
                .customerId(this.customerId)
                .orderAmount(this.orderAmount)
                .build();
    }
}
