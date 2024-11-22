package ru.itis.orders.repository;

import ru.itis.orders.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrdersRepository {
    Long save(Order order);

    Optional<Order> findById(Long id);

    List<Order> findByDateAfterAndTotalAmountGreaterThan(LocalDate startDate, BigDecimal minTotalAmount);

    List<Order> findExcludingProductAndDateBetween(Long productCode, LocalDate startDate, LocalDate endDate);
}
