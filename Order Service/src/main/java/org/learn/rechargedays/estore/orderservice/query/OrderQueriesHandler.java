package org.learn.rechargedays.estore.orderservice.query;

import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.learn.rechargedays.estore.orderservice.core.data.OrderEntity;
import org.learn.rechargedays.estore.orderservice.core.data.OrderRepository;
import org.learn.rechargedays.estore.orderservice.core.data.OrderSummary;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderQueriesHandler {

    private final OrderRepository orderRepository;

    @QueryHandler
    public OrderSummary findOrder(FindOrderQuery findOrderQuery) {
        OrderEntity orderEntity = orderRepository.findByOrderId(findOrderQuery.getOrderId());
        return new OrderSummary(orderEntity.getOrderId(), orderEntity.getOrderStatus(), "");

    }
}
