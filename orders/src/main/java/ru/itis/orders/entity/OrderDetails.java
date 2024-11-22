package ru.itis.orders.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetails {
    private Long id;
    private Long productCode;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private Long orderId;
}
