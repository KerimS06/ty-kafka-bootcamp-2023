package com.trendyol.kafkabootcamp2023.orderservice.model.request;

import com.trendyol.kafkabootcamp2023.orderservice.model.OrderStatus;

import java.util.Objects;

public class OrderStatusUpdateRequest {

    private Long orderId;
    private OrderStatus orderStatus;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "OrderStatusUpdateRequest{" +
                "orderId=" + orderId +
                ", orderStatus=" + orderStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderStatusUpdateRequest that = (OrderStatusUpdateRequest) o;
        return Objects.equals(orderId, that.orderId) && orderStatus == that.orderStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, orderStatus);
    }
}
