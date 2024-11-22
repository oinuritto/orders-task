package ru.itis.orders.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.orders.controller.api.OrdersApi;
import ru.itis.orders.dto.order.OrderRequestDto;
import ru.itis.orders.dto.order.OrderResponseDto;
import ru.itis.orders.dto.order.OrderWithDetailsResponseDto;
import ru.itis.orders.service.OrdersService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class OrdersController implements OrdersApi {
    private final OrdersService ordersService;

    @Override
    public ResponseEntity<Long> createOrder(OrderRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordersService.createOrder(requestDto));
    }

    @Override
    public ResponseEntity<OrderWithDetailsResponseDto> getOrderById(Long id) {
        return ResponseEntity.ok(ordersService.getOrder(id));
    }

    @Override
    public ResponseEntity<List<OrderResponseDto>> getOrdersByDateAfterAndTotalAmountGreaterThan(LocalDate startDate, BigDecimal minTotalAmount) {
        return ResponseEntity.ok(ordersService.getOrdersByDateAfterAndTotalAmountGreaterThan(startDate, minTotalAmount));
    }

    @Override
    public ResponseEntity<List<OrderResponseDto>> getOrdersExcludingProduct(Long productCode, LocalDate startDate, LocalDate endDate) {
        return ResponseEntity.ok(ordersService.getOrdersExcludingProductAndDateBetween(productCode, startDate, endDate));
    }
}
