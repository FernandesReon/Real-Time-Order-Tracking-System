package com.reon.order_backend.dto.order;

import com.reon.order_backend.document.Order;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderUpdateStatus {
    @NotNull(message = "Mention order status..")
    private Order.Status status;
}
