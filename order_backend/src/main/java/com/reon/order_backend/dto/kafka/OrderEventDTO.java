package com.reon.order_backend.dto.kafka;

import com.reon.order_backend.document.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEventDTO {
    private ObjectId orderId;
    private ObjectId userId;
    private String email;
    private LocalDateTime eventCreationTime;
    private List<String> items;
    private Double amount;
    private Order.Status status;
}
