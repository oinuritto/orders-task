package ru.itis.orders.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.orders.dto.orderDetails.OrderDetailsResponseDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "Order with details response")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderWithDetailsResponseDto {
    @Schema(description = "ID", example = "1")
    private Long id;
    @Schema(description = "Order number", example = "1111120241231")
    private String orderNumber;
    @Schema(description = "Total amount", example = "100.00")
    private BigDecimal totalAmount;
    @Schema(description = "Order date", example = "2021-09-01")
    private LocalDate orderDate;
    @Schema(description = "Recipient name", example = "John Doe")
    private String recipient;
    @Schema(description = "Delivery address", example = "Moscow, Red Square")
    private String deliveryAddress;
    @Schema(description = "Payment type", example = "Cash")
    private String paymentType;
    @Schema(description = "Delivery type", example = "Courier")
    private String deliveryType;
    @Schema(description = "Order items")
    private List<OrderDetailsResponseDto> items;
}
