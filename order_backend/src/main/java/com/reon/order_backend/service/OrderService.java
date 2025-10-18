package com.reon.order_backend.service;

import com.reon.order_backend.document.User;
import com.reon.order_backend.dto.order.OrderCreation;
import com.reon.order_backend.dto.order.OrderResponse;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

public interface OrderService {
    OrderResponse createOrder(OrderCreation orderCreation, ObjectId id);
    Page<OrderResponse> fetchAllOrders(int pageNo, int pageSize, User user);
    void cancelOrder(ObjectId orderId, User user);

    OrderResponse fetchOrderViaId(ObjectId id, User user);
}
