package com.reon.order_backend.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user_orders")
public class Order {
    @Id
    private ObjectId id;
    private List<String> items = new ArrayList<>();
    private Double amount;
    private Status status = Status.PENDING;

    public enum Status {
        PENDING,
        PROCESSING,
        PACKED,
        PICKED,
        READY_FOR_DISPATCH,
        SHIPPED,
        OUT_FOR_DELIVERY
    }

    private Map<String, LocalDateTime> timeStamps = new HashMap<>();

    @CreatedDate
    private LocalDateTime createdOn;
    @LastModifiedDate
    private LocalDateTime updateOn;
}
