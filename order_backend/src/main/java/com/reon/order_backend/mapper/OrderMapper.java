package com.reon.order_backend.mapper;

import com.reon.order_backend.document.Order;
import com.reon.order_backend.dto.order.OrderCreation;
import com.reon.order_backend.dto.order.OrderResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public static Order mapOrderToEntity(OrderCreation createOrder) {
        return Order.builder()
                .items(createOrder.getItems())
                .amount(createOrder.getAmount())
                .build();
    }

    public static OrderResponse orderResponseToUser(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .items(order.getItems())
                .amount(order.getAmount())
                .status(order.getStatus())
                .timeStamps(order.getTimeStamps())
                .createdOn(order.getCreatedOn())
                .updateOn(order.getUpdateOn())
                .build();
    }
}
