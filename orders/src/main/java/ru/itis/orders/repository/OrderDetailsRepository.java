package ru.itis.orders.repository;

import ru.itis.orders.entity.OrderDetails;

import java.util.List;

public interface OrderDetailsRepository {
    void save(List<OrderDetails> orderDetailsList);

    List<OrderDetails> findAllByOrderId(Long orderId);
}
