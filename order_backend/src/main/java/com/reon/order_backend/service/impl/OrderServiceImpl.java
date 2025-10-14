package com.reon.order_backend.service.impl;

import com.reon.order_backend.document.Order;
import com.reon.order_backend.document.User;
import com.reon.order_backend.dto.order.OrderCreation;
import com.reon.order_backend.dto.order.OrderResponse;
import com.reon.order_backend.exception.OrderNotFoundException;
import com.reon.order_backend.exception.UserNotFoundException;
import com.reon.order_backend.mapper.OrderMapper;
import com.reon.order_backend.repository.OrderRepository;
import com.reon.order_backend.repository.UserRepository;
import com.reon.order_backend.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public OrderResponse createOrder(OrderCreation orderCreation, ObjectId id) {
        log.info("Order Service :: Order creation in progress..");
        Order order = OrderMapper.mapOrderToEntity(orderCreation);
        order.setUserId(id);
        order.setStatus(Order.Status.PENDING);

        // TODO: find a way to dynamically update the status..
        Map<String, LocalDateTime> timeStamp = new HashMap<>();
        timeStamp.put("PENDING", LocalDateTime.now());
        order.setTimeStamps(timeStamp);

        Order saveOrder = orderRepository.save(order);

        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User with provided detail not found.")
        );
        user.getOrderList().add(saveOrder);
        userRepository.save(user);

        return OrderMapper.orderResponseToUser(saveOrder);
    }

    @Override
    public Page<OrderResponse> fetchAllOrders(int pageNo, int pageSize, User user) {
        log.info("Order Service :: Fetching orders for user ID: {}, page: {}, size: {}", user.getId(), pageNo, pageSize);
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<Order> orders = orderRepository.findByUserId(user.getId(), pageable);
        return orders.map(OrderMapper::orderResponseToUser);
    }

    @Override
    public void cancelOrder(ObjectId orderId) {
        log.warn("Order Service :: Cancelling order with id: {}", orderId);
        orderRepository.deleteById(orderId);
    }

    @Override
    public OrderResponse fetchOrderViaId(ObjectId id) {
        log.info("Order Service :: Fetching order with id: {}", id);
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new OrderNotFoundException("Order not found with id: " + id)
        );
        return OrderMapper.orderResponseToUser(order);
    }
}
