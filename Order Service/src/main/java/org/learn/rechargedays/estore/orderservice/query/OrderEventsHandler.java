package org.learn.rechargedays.estore.orderservice.query;

import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.learn.rechargedays.estore.orderservice.core.data.OrderEntity;
import org.learn.rechargedays.estore.orderservice.core.data.OrderRepository;
import org.learn.rechargedays.estore.orderservice.core.events.OrderCreatedEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup("order-group")
public class OrderEventsHandler {

    private final OrderRepository orderRepository;

    @EventHandler
    public void on(OrderCreatedEvent orderCreatedEvent) {
        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(orderCreatedEvent, orderEntity);
        orderRepository.save(orderEntity);
    }
}
