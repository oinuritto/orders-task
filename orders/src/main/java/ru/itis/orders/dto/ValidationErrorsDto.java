package ru.itis.orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "Validation errors response")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidationErrorsDto {
    @Schema(description = "Validation errors")
    private List<ValidationErrorDto> errors;
}
