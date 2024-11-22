package ru.itis.orders.service;

import ru.itis.orders.dto.order.OrderRequestDto;
import ru.itis.orders.dto.order.OrderResponseDto;
import ru.itis.orders.dto.order.OrderWithDetailsResponseDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface OrdersService {
    Long createOrder(OrderRequestDto requestDto);

    OrderWithDetailsResponseDto getOrder(Long id);

    List<OrderResponseDto> getOrdersByDateAfterAndTotalAmountGreaterThan(LocalDate startDate, BigDecimal minTotalAmount);

    List<OrderResponseDto> getOrdersExcludingProductAndDateBetween(Long productCode, LocalDate startDate, LocalDate endDate);
}
