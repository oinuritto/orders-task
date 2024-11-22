package ru.itis.orders.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.orders.dto.orderDetails.OrderDetailsRequestDto;

import java.util.List;

@Schema(description = "Order request")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderRequestDto {
    @Schema(description = "Recipient name", example = "John Doe")
    @NotBlank
    private String recipient;

    @Schema(description = "Delivery address", example = "Moscow, Red Square")
    @NotBlank
    private String deliveryAddress;

    @Schema(description = "Payment type", example = "Cash")
    @NotBlank
    private String paymentType;

    @Schema(description = "Delivery type", example = "Courier")
    @NotBlank
    private String deliveryType;

    @Schema(description = "Order items")
    @NotEmpty
    private List<@Valid OrderDetailsRequestDto> items;
}
