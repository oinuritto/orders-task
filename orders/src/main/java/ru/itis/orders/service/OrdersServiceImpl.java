package ru.itis.orders.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.orders.dto.order.OrderRequestDto;
import ru.itis.orders.dto.order.OrderResponseDto;
import ru.itis.orders.dto.order.OrderWithDetailsResponseDto;
import ru.itis.orders.dto.orderDetails.OrderDetailsRequestDto;
import ru.itis.orders.entity.Order;
import ru.itis.orders.entity.OrderDetails;
import ru.itis.orders.exception.NotFoundException;
import ru.itis.orders.mapper.OrderMapper;
import ru.itis.orders.repository.OrderDetailsRepository;
import ru.itis.orders.repository.OrdersRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {
    private final OrdersRepository ordersRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final NumbersServiceClient numbersServiceClient;
    private final OrderMapper orderMapper = OrderMapper.INSTANCE;

    @Override
    public Long createOrder(OrderRequestDto requestDto) {
        BigDecimal totalAmount = calcTotalAmountOfOrder(requestDto.getItems());

        Order order = orderMapper.toOrder(requestDto);
        order.setOrderDate(LocalDate.now());
        order.setOrderNumber(numbersServiceClient.getOrderNumber());
        order.setTotalAmount(totalAmount);

        Long orderId = ordersRepository.save(order);

        List<OrderDetails> orderDetailsList = orderMapper.toOrderDetailsList(requestDto.getItems(), orderId);

        orderDetailsRepository.save(orderDetailsList);
        return orderId;
    }

    @Override
    public OrderWithDetailsResponseDto getOrder(Long id) {
        Order order = ordersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order with id <" + id + "> not found"));
        List<OrderDetails> orderDetails = orderDetailsRepository.findAllByOrderId(id);

        return orderMapper.toOrderWithDetailsResponseDto(order, orderMapper.toOrderDetailsResponseDtoList(orderDetails));
    }

    @Override
    public List<OrderResponseDto> getOrdersByDateAfterAndTotalAmountGreaterThan(LocalDate startDate, BigDecimal minTotalAmount) {
        List<Order> orders = ordersRepository.findByDateAfterAndTotalAmountGreaterThan(startDate, minTotalAmount);
        return orderMapper.toOrderResponseDtoList(orders);
    }

    @Override
    public List<OrderResponseDto> getOrdersExcludingProductAndDateBetween(Long productCode, LocalDate startDate, LocalDate endDate) {
        List<Order> orders = ordersRepository.findExcludingProductAndDateBetween(productCode, startDate, endDate);
        return orderMapper.toOrderResponseDtoList(orders);
    }

    private BigDecimal calcTotalAmountOfOrder(List<OrderDetailsRequestDto> items) {
        return items.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
