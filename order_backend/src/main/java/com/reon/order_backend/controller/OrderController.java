package com.reon.order_backend.controller;

import com.reon.order_backend.document.User;
import com.reon.order_backend.dto.order.OrderCreation;
import com.reon.order_backend.dto.order.OrderResponse;
import com.reon.order_backend.dto.order.OrderUpdateStatus;
import com.reon.order_backend.exception.UserNotFoundException;
import com.reon.order_backend.repository.UserRepository;
import com.reon.order_backend.service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(
        name = "endpoint related to orders, accessible after authentication",
        path = "/api/v1/order"
)
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final UserRepository userRepository;

    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(
            name = "endpoint for generating new order",
            path = "/generateOrder"
    )
    public ResponseEntity<OrderResponse> generateOrder(@Valid @RequestBody OrderCreation createOrder,
                                                       Principal principal) {
        log.info("Order Controller :: Incoming request for generating new order: {}", createOrder);
        User user = userRepository.findByEmail(principal.getName()).orElseThrow(
                () -> new UserNotFoundException("User not found with provided details.")
        );
        OrderResponse response = orderService.createOrder(createOrder, user.getId());
        log.info("Order Controller :: Order generation: {} was successful for id: {}", createOrder, user.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(
            name = "endpoint for fetching all orders",
            path = "/orders"
    )
    public ResponseEntity<Page<OrderResponse>> fetchOrders(@RequestParam(name = "page", defaultValue = "0") int pageNo,
                                                           @RequestParam(name = "size", defaultValue = "10") int pageSize,
                                                           Principal principal) {
        log.info("Order Controller :: Incoming request for fetching order from page: {} of size: {}", pageNo, pageSize);
        User user = userRepository.findByEmail(principal.getName()).orElseThrow(
                () -> new UserNotFoundException("User with provided details not found.")
        );
        Page<OrderResponse> orders = orderService.fetchAllOrders(pageNo, pageSize, user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orders);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping(
            name = "endpoint for cancelling order",
            path = "/cancel/{orderId}"
    )
    public ResponseEntity<Void> cancelOrder(@PathVariable(name = "orderId")ObjectId orderId, Principal principal) {
        log.info("Order Controller :: Incoming request for cancelling order with id: {}", orderId);
        User user = userRepository.findByEmail(principal.getName()).orElseThrow(
                () -> new UserNotFoundException("User with provided details not found.")
        );
        orderService.cancelOrder(orderId, user);

        log.info("Order Controller :: Order with id: {} is cancelled successfully.", orderId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(
            name = "endpoint for fetching specific order",
            path = "/fetch/{orderId}"
    )
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable(name = "orderId")ObjectId orderId,
                                                      Principal principal) {
        log.info("Order Controller :: Incoming request for fetching order with id: {}", orderId);

        User user = userRepository.findByEmail(principal.getName()).orElseThrow(
                () -> new UserNotFoundException("User with provided details not found.")
        );

        OrderResponse fetchedOrder = orderService.fetchOrderViaId(orderId, user);
        log.info("Order Controller :: Order with id: {} is fetched successfully.", orderId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(fetchedOrder);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping(
            name = "endpoint for updating the order status",
            path = "/update/{orderId}"
    )
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable(name = "orderId") ObjectId orderId,
                                                           @Valid @RequestBody OrderUpdateStatus orderUpdateStatus,
                                                           Principal principal) {
        log.info("Order Controller :: Incoming request for updating order status");

        User user = userRepository.findByEmail(principal.getName()).orElseThrow(
                () -> new UserNotFoundException("User with provided details not found.")
        );

        OrderResponse updatedOrder = orderService.updateOrder(orderId, orderUpdateStatus, user);
        log.info("Order Controller :: Order with id: {} is updated successfully.", orderId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedOrder);
    }
}
