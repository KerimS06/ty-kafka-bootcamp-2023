package com.trendyol.kafkabootcamp2023.orderservice.repository;

import com.trendyol.kafkabootcamp2023.orderservice.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
