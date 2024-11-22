package ru.itis.orders.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.orders.dto.ExceptionDto;
import ru.itis.orders.dto.order.OrderRequestDto;
import ru.itis.orders.dto.order.OrderResponseDto;
import ru.itis.orders.dto.order.OrderWithDetailsResponseDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "Orders", description = "Controller for orders")
@RequestMapping("/orders")
public interface OrdersApi {
    @Operation(summary = "Create order")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order created"),
    })
    @PostMapping
    ResponseEntity<Long> createOrder(@Parameter(description = "Order info") @Valid @RequestBody OrderRequestDto requestDto);

    @Operation(summary = "Get order by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order found", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderWithDetailsResponseDto.class))
            }),
            @ApiResponse(responseCode = "404", description = "Order not found", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class))
            })
    })
    @GetMapping("/{id}")
    ResponseEntity<OrderWithDetailsResponseDto> getOrderById(@Parameter(description = "Order ID") @PathVariable Long id);

    @Operation(summary = "Get orders by date and total amount greater than specified value")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders found",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = OrderResponseDto.class))))
    })
    @GetMapping("/search")
    ResponseEntity<List<OrderResponseDto>> getOrdersByDateAfterAndTotalAmountGreaterThan(
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Total amount from") @RequestParam BigDecimal minTotalAmount);

    @Operation(summary = "Get orders excluding a specific product and with a date between the specified range")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders found",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = OrderResponseDto.class))))
    })
    @GetMapping("/exclude-product")
    ResponseEntity<List<OrderResponseDto>> getOrdersExcludingProduct(
            @Parameter(description = "Excluding product code") @RequestParam Long productCode,
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate);
}

