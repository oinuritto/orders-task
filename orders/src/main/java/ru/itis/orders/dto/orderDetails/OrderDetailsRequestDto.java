package ru.itis.orders.dto.orderDetails;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(description = "Order details request")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderDetailsRequestDto {
    @Schema(description = "Product code", example = "1")
    @Min(0L)
    private Long productCode;

    @Schema(description = "Product name", example = "Product")
    @NotBlank
    private String productName;

    @Schema(description = "Quantity", example = "1")
    @NotNull
    @PositiveOrZero
    private Integer quantity;

    @Schema(description = "Unit price", example = "100.00")
    @NotNull
    @PositiveOrZero
    private BigDecimal unitPrice;
}
