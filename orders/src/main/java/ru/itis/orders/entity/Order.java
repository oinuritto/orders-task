package ru.itis.orders.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order {
    private Long id;
    private String orderNumber;
    private BigDecimal totalAmount;
    private LocalDate orderDate;
    private String recipient;
    private String deliveryAddress;
    private String paymentType; // enum?
    private String deliveryType; // enum?
}
