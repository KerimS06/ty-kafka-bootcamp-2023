package com.trendyol.kafkabootcamp2023.orderservice.service;

import com.trendyol.kafkabootcamp2023.orderservice.domain.Payment;
import com.trendyol.kafkabootcamp2023.orderservice.messaging.message.OrderDeliveredMessage;
import com.trendyol.kafkabootcamp2023.orderservice.repository.PaymentRepository;
import com.trendyol.kafkabootcamp2023.orderservice.util.Clock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    PaymentService paymentService;

    @Mock
    PaymentRepository paymentRepository;

    @Test
    public void it_should_create_payment() {
        //given
        Clock.freeze();
        OrderDeliveredMessage orderDeliveredMessage = new OrderDeliveredMessage();
        orderDeliveredMessage.setSellerId(123L);
        orderDeliveredMessage.setAmount(BigDecimal.TEN);
        orderDeliveredMessage.setOrderId(3L);

        Payment payment = new Payment();
        payment.setId(1L);
        payment.setAmount(BigDecimal.TEN);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        //when
        paymentService.createPayment(orderDeliveredMessage);

        //then
        var argumentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(argumentCaptor.capture());
        Payment resultPayment = argumentCaptor.getValue();
        assertThat(resultPayment.getSellerId()).isEqualTo(orderDeliveredMessage.getSellerId());
        assertThat(resultPayment.getAmount()).isEqualByComparingTo(orderDeliveredMessage.getAmount());
        assertThat(resultPayment.getPaymentDate()).isEqualTo(Clock.now().toDate());
        Clock.unfreeze();

    }
}