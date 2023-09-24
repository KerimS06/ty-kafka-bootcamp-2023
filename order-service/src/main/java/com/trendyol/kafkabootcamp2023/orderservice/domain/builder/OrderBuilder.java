package com.trendyol.kafkabootcamp2023.orderservice.domain.builder;

import com.trendyol.kafkabootcamp2023.orderservice.domain.Order;
import com.trendyol.kafkabootcamp2023.orderservice.model.OrderStatus;
import com.trendyol.kafkabootcamp2023.orderservice.model.OrderType;
import com.trendyol.kafkabootcamp2023.orderservice.util.Clock;

import java.math.BigDecimal;
import java.util.Date;

public final class OrderBuilder {
    private Long id;
    private Date orderDate;
    private Long customerId;
    private Long sellerId;
    private OrderType orderType;
    private OrderStatus orderStatus;
    private BigDecimal orderAmount;
    private Date createdDate = Clock.now().toDate();
    private Date lastModifiedDate = Clock.now().toDate();

    private OrderBuilder() {
    }

    public static OrderBuilder anOrder() {
        return new OrderBuilder();
    }

    public OrderBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public OrderBuilder orderDate(Date orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public OrderBuilder customerId(Long customerId) {
        this.customerId = customerId;
        return this;
    }

    public OrderBuilder sellerId(Long sellerId) {
        this.sellerId = sellerId;
        return this;
    }

    public OrderBuilder orderType(OrderType orderType) {
        this.orderType = orderType;
        return this;
    }

    public OrderBuilder orderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public OrderBuilder orderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
        return this;
    }

    public OrderBuilder createdDate(Date createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public OrderBuilder lastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public Order build() {
        Order order = new Order();
        order.setId(id);
        order.setOrderDate(orderDate);
        order.setCustomerId(customerId);
        order.setSellerId(sellerId);
        order.setOrderType(orderType);
        order.setOrderStatus(orderStatus);
        order.setOrderAmount(orderAmount);
        order.setCreatedDate(createdDate);
        order.setLastModifiedDate(lastModifiedDate);
        return order;
    }
}
