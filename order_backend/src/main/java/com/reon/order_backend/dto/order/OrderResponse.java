package com.reon.order_backend.dto.order;

import com.reon.order_backend.document.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private ObjectId id;
    private ObjectId userId;
    private List<String> items;
    private Double amount;
    private Order.Status status;
    private Map<String, LocalDateTime> timeStamps;
    private LocalDateTime createdOn;
    private LocalDateTime updateOn;
}
