package ru.itis.orders.dto.orderDetails;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(description = "Order details response")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderDetailsResponseDto {
    @Schema(description = "ID", example = "1")
    private Long id;
    @Schema(description = "Product code", example = "1")
    private Long productCode;
    @Schema(description = "Product name", example = "Product")
    private String productName;
    @Schema(description = "Quantity", example = "1")
    private Integer quantity;
    @Schema(description = "Unit price", example = "100.00")
    private BigDecimal unitPrice;
}
