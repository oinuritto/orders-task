package ru.itis.orders.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Validation error response")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationErrorDto {
    @Schema(description = "Field name", example = "productName")
    private String fieldName;
    @Schema(description = "Error message", example = "must not be blank")
    private String message;
}
