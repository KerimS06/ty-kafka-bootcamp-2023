package com.trendyol.kafkabootcamp2023.orderservice.service;

import com.trendyol.kafkabootcamp2023.orderservice.domain.Payment;
import com.trendyol.kafkabootcamp2023.orderservice.messaging.message.OrderDeliveredMessage;
import com.trendyol.kafkabootcamp2023.orderservice.repository.PaymentRepository;
import com.trendyol.kafkabootcamp2023.orderservice.util.Clock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public void createPayment(OrderDeliveredMessage message) {
        logger.info("Payment will be created for seller id: {}, message: {}", message.getSellerId(), message);
        Payment payment = new Payment();
        payment.setSellerId(message.getSellerId());
        payment.setAmount(message.getAmount());
        payment.setPaymentDate(Clock.now().toDate());
        Payment savedPayment = paymentRepository.save(payment);
        logger.info("A payment is created for seller id: {}, payment: {}", savedPayment.getSellerId(), savedPayment);
    }


}
