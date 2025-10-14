package com.reon.order_backend.dto.order;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreation {
    @NotEmpty(message = "Order must contain at least one item")
    private List<String> items;

    @DecimalMin(value = "0.0", inclusive = false, message = "Amount should be non-negative")
    private Double amount;
}
