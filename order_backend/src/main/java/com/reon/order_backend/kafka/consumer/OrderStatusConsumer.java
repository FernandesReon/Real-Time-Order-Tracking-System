package com.reon.order_backend.kafka.consumer;

import com.reon.order_backend.document.FailedEvent;
import com.reon.order_backend.dto.kafka.OrderEventDTO;
import com.reon.order_backend.email.EmailService;
import com.reon.order_backend.repository.FailedEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class OrderStatusConsumer {

    private final EmailService emailService;
    private final FailedEventRepository failedEventRepository;

    public OrderStatusConsumer(EmailService emailService, FailedEventRepository failedEventRepository) {
        this.emailService = emailService;
        this.failedEventRepository = failedEventRepository;
    }

    @RetryableTopic(
            attempts = "5",
            backoff = @Backoff(delay = 5000, multiplier = 2),
            dltStrategy = DltStrategy.FAIL_ON_ERROR
    )
    @KafkaListener(
            topics = "order_event",
            groupId = "grp_orders"
    )
    public void orderPlaceConsumer(OrderEventDTO orderEventDTO) {
        log.info("Order Placed: {}", orderEventDTO);
        sendOrderPlaceEmail(orderEventDTO);
    }

    private void sendOrderPlaceEmail(OrderEventDTO orderEventDTO) {
        StringBuilder items = new StringBuilder();
        orderEventDTO.getItems().forEach(item -> items.append("- ").append(item).append("\n"));

        /*
        extracted values from order event dto
         */
        double amount = orderEventDTO.getAmount();
        String receiverEmailId = orderEventDTO.getEmail();
        String status = String.valueOf(orderEventDTO.getStatus());
        String createdOn = String.valueOf(orderEventDTO.getEventCreationTime());

        // email body
        String emailBody = """
                Hello there,
                
                Your order has been placed, here are the details:
                
                Items:
                %s
                
                Amount: %.2f
                Status: %s
                CreatedOn: %s
                
                """.formatted(items, amount, status, createdOn);

        emailService.sendOrderSuccessEmail(receiverEmailId, "Order Placed", emailBody);
        log.info("Order Consumer :: Order Placed email sent to: {}",receiverEmailId);
    }

    @RetryableTopic(
            attempts = "5",
            backoff = @Backoff(delay = 5000, multiplier = 2),
            dltStrategy = DltStrategy.FAIL_ON_ERROR
    )
    @KafkaListener(topics = "order_update_event", groupId = "grp_orders")
    public void orderStatusConsumer(OrderEventDTO orderEventDTO) {
        log.info("Order Status: {}", orderEventDTO);
        sendOrderStatusEmail(orderEventDTO);
    }

    private void sendOrderStatusEmail(OrderEventDTO orderEventDTO) {
        String newOrderStatus = orderEventDTO.getStatus().toString();
        ObjectId orderId = orderEventDTO.getOrderId();

        String emailBody = """
                Hello there,
                
                Your order status has been updated:
                OrderId: %s
                Status: %s
                
                """.formatted(orderId, newOrderStatus);
        emailService.sendOrderStatusEmail(orderEventDTO.getEmail(), "Order Status", emailBody);
        log.info("Order Consumer :: Order status is updated to: {}", newOrderStatus);
    }

    @DltHandler
    public void handleOrderPlacedDLT(OrderEventDTO dltEvent) {
        log.error("Order event failed after retries: {}", dltEvent);

        FailedEvent failedEvent = FailedEvent.builder()
                .topic("order_event")
                .payload(dltEvent)
                .errorMessage("")
                .failedAt(LocalDateTime.now())
                .build();
        failedEventRepository.save(failedEvent);

        // todo:: send alert to admin
    }

}
