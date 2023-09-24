package com.trendyol.kafkabootcamp2023.orderservice.repository;

import com.trendyol.kafkabootcamp2023.orderservice.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
