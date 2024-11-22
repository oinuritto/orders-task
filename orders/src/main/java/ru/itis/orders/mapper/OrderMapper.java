package ru.itis.orders.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.itis.orders.dto.order.OrderResponseDto;
import ru.itis.orders.dto.orderDetails.OrderDetailsRequestDto;
import ru.itis.orders.dto.orderDetails.OrderDetailsResponseDto;
import ru.itis.orders.dto.order.OrderRequestDto;
import ru.itis.orders.dto.order.OrderWithDetailsResponseDto;
import ru.itis.orders.entity.Order;
import ru.itis.orders.entity.OrderDetails;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
    List<OrderResponseDto> toOrderResponseDtoList(List<Order> orders);

    OrderWithDetailsResponseDto toOrderWithDetailsResponseDto(Order order, List<OrderDetailsResponseDto> items);

    OrderDetailsResponseDto toOrderDetailsResponseDto(OrderDetails orderDetail);

    List<OrderDetailsResponseDto> toOrderDetailsResponseDtoList(List<OrderDetails> orderDetails);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "orderNumber", ignore = true)
    Order toOrder(OrderRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderId", ignore = true)
    OrderDetails toOrderDetails(OrderDetailsRequestDto dto);

    default List<OrderDetails> toOrderDetailsList(List<OrderDetailsRequestDto> items, Long orderId) {
        if (items == null) {
            return null;
        }
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        for (OrderDetailsRequestDto item : items) {
            OrderDetails orderDetails = toOrderDetails(item);
            orderDetails.setOrderId(orderId);
            orderDetailsList.add(orderDetails);
        }
        return orderDetailsList;
    }
}

