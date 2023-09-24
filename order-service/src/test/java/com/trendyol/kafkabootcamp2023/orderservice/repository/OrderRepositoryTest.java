package com.trendyol.kafkabootcamp2023.orderservice.repository;

import com.trendyol.kafkabootcamp2023.orderservice.AbstractContainerBaseTest;
import com.trendyol.kafkabootcamp2023.orderservice.domain.Order;
import com.trendyol.kafkabootcamp2023.orderservice.domain.builder.OrderBuilder;
import com.trendyol.kafkabootcamp2023.orderservice.model.OrderStatus;
import com.trendyol.kafkabootcamp2023.orderservice.model.OrderType;
import com.trendyol.kafkabootcamp2023.orderservice.util.Clock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryTest extends AbstractContainerBaseTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void it_should_find_order_by_id() {
        //given
        Clock.freeze();
        Order order = OrderBuilder.anOrder()
                .id(1L)
                .orderDate(Clock.now().minusDays(1).toDate())
                .sellerId(123L)
                .customerId(100L)
                .orderType(OrderType.CORE)
                .orderStatus(OrderStatus.CREATED)
                .orderAmount(BigDecimal.TEN)
                .build();

        orderRepository.save(order);
        testEntityManager.flush();
        testEntityManager.clear();

        //when
        Optional<Order> orderOptional = orderRepository.findById(1L);

        //then
        assertThat(orderOptional).isPresent();
        assertThat(orderOptional.get().getId()).isEqualTo(1L);
    }

}