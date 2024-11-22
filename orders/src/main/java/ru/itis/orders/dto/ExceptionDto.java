package ru.itis.orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "Exception response")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ExceptionDto {
    @Schema(description = "Error message", example = "Order not found")
    private String message;
}
