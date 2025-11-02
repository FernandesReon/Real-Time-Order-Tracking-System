package com.reon.order_backend.service;

import com.reon.order_backend.document.User;
import com.reon.order_backend.dto.order.OrderCreation;
import com.reon.order_backend.dto.order.OrderResponse;
import com.reon.order_backend.dto.order.OrderUpdateStatus;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

public interface OrderService {
    OrderResponse createOrder(OrderCreation orderCreation, ObjectId id);
    Page<OrderResponse> fetchAllOrders(int pageNo, int pageSize, User user);
    void cancelOrder(ObjectId orderId, User user);
    OrderResponse updateOrder(ObjectId orderId, OrderUpdateStatus orderUpdateStatus, User user);
    OrderResponse fetchOrderViaId(ObjectId id, User user);
}
