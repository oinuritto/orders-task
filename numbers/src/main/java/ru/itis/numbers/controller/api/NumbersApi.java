package ru.itis.numbers.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Order numbers", description = "Generate and get order number")
@RequestMapping("/numbers")
public interface NumbersApi {
    @Operation(summary = "Get generated order number")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Succesfully got generated order number")
    })
    @GetMapping
    ResponseEntity<String> getOrderNumbers();
}
