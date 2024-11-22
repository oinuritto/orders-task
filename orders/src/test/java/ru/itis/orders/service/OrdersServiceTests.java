package ru.itis.orders.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.itis.orders.dto.order.OrderRequestDto;
import ru.itis.orders.dto.order.OrderResponseDto;
import ru.itis.orders.dto.order.OrderWithDetailsResponseDto;
import ru.itis.orders.dto.orderDetails.OrderDetailsRequestDto;
import ru.itis.orders.entity.Order;
import ru.itis.orders.entity.OrderDetails;
import ru.itis.orders.exception.NotFoundException;
import ru.itis.orders.repository.OrderDetailsRepository;
import ru.itis.orders.repository.OrdersRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrdersServiceTests {
    @Mock
    private OrderDetailsRepository orderDetailsRepository;
    @Mock
    private OrdersRepository ordersRepository;
    @Mock
    private NumbersServiceClient numbersServiceClient;
    @InjectMocks
    private OrdersServiceImpl ordersService;
    private Order order;
    private List<OrderDetails> orderDetailsList;

    @BeforeEach
    public void init() {
        this.order = Order.builder()
                .deliveryType("delivery")
                .recipient("recipient")
                .deliveryAddress("address")
                .paymentType("payment")
                .orderNumber("1111120241212")
                .orderDate(LocalDate.now())
                .totalAmount(BigDecimal.valueOf(5.0))
                .id(1L).build();

        this.orderDetailsList = List.of(
                OrderDetails.builder()
                        .productCode(1L)
                        .unitPrice(BigDecimal.valueOf(1.0))
                        .productName("product")
                        .quantity(1)
                        .orderId(order.getId())
                        .build(),
                OrderDetails.builder()
                        .productCode(2L)
                        .unitPrice(BigDecimal.valueOf(2.0))
                        .productName("product2")
                        .quantity(2)
                        .orderId(order.getId())
                        .build());
    }

    @Test
    public void testCreate() {
        OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                .recipient(order.getRecipient())
                .deliveryAddress(order.getDeliveryAddress())
                .deliveryType(order.getDeliveryType())
                .paymentType(order.getPaymentType())
                .items(List.of(
                        OrderDetailsRequestDto.builder()
                                .productCode(orderDetailsList.get(0).getProductCode())
                                .productName(orderDetailsList.get(0).getProductName())
                                .quantity(orderDetailsList.get(0).getQuantity())
                                .unitPrice(orderDetailsList.get(0).getUnitPrice())
                                .build(),
                        OrderDetailsRequestDto.builder()
                                .productCode(orderDetailsList.get(1).getProductCode())
                                .productName(orderDetailsList.get(1).getProductName())
                                .quantity(orderDetailsList.get(1).getQuantity())
                                .unitPrice(orderDetailsList.get(1).getUnitPrice())
                                .build()
                )).build();

        Long id = order.getId();
        when(ordersRepository.save(any(Order.class))).thenReturn(id);
        when(numbersServiceClient.getOrderNumber()).thenReturn(order.getOrderNumber());

        Long resultId = ordersService.createOrder(orderRequestDto);

        verify(numbersServiceClient).getOrderNumber();
        verify(ordersRepository).save(any(Order.class));
        verify(orderDetailsRepository).save(anyList());

        verify(ordersRepository).save(argThat(savedOrder -> {
            assertEquals(order.getOrderNumber(), savedOrder.getOrderNumber());
            assertNotNull(savedOrder.getOrderDate());
            assertEquals(order.getTotalAmount(), savedOrder.getTotalAmount());
            return true;
        }));

        verify(orderDetailsRepository).save(argThat(details -> {
            assertEquals(2, details.size());
            assertEquals(orderDetailsList.get(0).getProductCode(), details.get(0).getProductCode());
            assertEquals(orderDetailsList.get(1).getProductName(), details.get(1).getProductName());
            return true;
        }));

        assertEquals(id, resultId);
    }

    @Test
    public void testGetById() {
        Long id = order.getId();
        when(ordersRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderDetailsRepository.findAllByOrderId(id)).thenReturn(orderDetailsList);

        OrderWithDetailsResponseDto result = ordersService.getOrder(id);

        verify(ordersRepository).findById(id);
        verify(orderDetailsRepository).findAllByOrderId(id);

        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        assertEquals(orderDetailsList.size(), result.getItems().size());
    }

    @Test
    public void testGetById_throwsNotFoundException() {
        Long id = order.getId();
        when(ordersRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> ordersService.getOrder(id));
    }

    @Test
    public void testGetOrdersByDateAfterAndTotalAmountGreaterThan() {
        LocalDate startDate = order.getOrderDate().minusDays(1);
        BigDecimal minAmount = order.getTotalAmount().subtract(BigDecimal.valueOf(1.0));
        when(ordersRepository.findByDateAfterAndTotalAmountGreaterThan(startDate, minAmount)).thenReturn(List.of(order));

        List<OrderResponseDto> result = ordersService.getOrdersByDateAfterAndTotalAmountGreaterThan(startDate, minAmount);

        verify(ordersRepository).findByDateAfterAndTotalAmountGreaterThan(startDate, minAmount);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetOrdersByDateAfterAndTotalAmountGreaterThan_wrongDate() {
        LocalDate startDate = order.getOrderDate().plusDays(1);
        BigDecimal minAmount = order.getTotalAmount().subtract(BigDecimal.valueOf(1.0));
        when(ordersRepository.findByDateAfterAndTotalAmountGreaterThan(startDate, minAmount)).thenReturn(Collections.emptyList());

        List<OrderResponseDto> result = ordersService.getOrdersByDateAfterAndTotalAmountGreaterThan(startDate, minAmount);

        verify(ordersRepository).findByDateAfterAndTotalAmountGreaterThan(startDate, minAmount);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetOrdersByDateAfterAndTotalAmountGreaterThan_wrongTotalAmount() {
        LocalDate startDate = order.getOrderDate().minusDays(1);
        BigDecimal minAmount = order.getTotalAmount().add(BigDecimal.valueOf(1.0));
        when(ordersRepository.findByDateAfterAndTotalAmountGreaterThan(startDate, minAmount)).thenReturn(Collections.emptyList());

        List<OrderResponseDto> result = ordersService.getOrdersByDateAfterAndTotalAmountGreaterThan(startDate, minAmount);

        verify(ordersRepository).findByDateAfterAndTotalAmountGreaterThan(startDate, minAmount);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetOrdersExcludingProductAndDateBetween() {
        Long productCode = 0L;
        LocalDate startDate = order.getOrderDate().minusDays(2);
        LocalDate endDate = order.getOrderDate().plusDays(2);
        when(ordersRepository.findExcludingProductAndDateBetween(productCode, startDate, endDate)).thenReturn(List.of(order));

        List<OrderResponseDto> result = ordersService.getOrdersExcludingProductAndDateBetween(productCode, startDate, endDate);

        verify(ordersRepository).findExcludingProductAndDateBetween(productCode, startDate, endDate);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetOrdersExcludingProductAndDateBetween_excludeProduct() {
        Long productCode = 1L;
        LocalDate startDate = order.getOrderDate().minusDays(2);
        LocalDate endDate = order.getOrderDate().plusDays(2);
        when(ordersRepository.findExcludingProductAndDateBetween(productCode, startDate, endDate)).thenReturn(Collections.emptyList());

        List<OrderResponseDto> result = ordersService.getOrdersExcludingProductAndDateBetween(productCode, startDate, endDate);

        verify(ordersRepository).findExcludingProductAndDateBetween(productCode, startDate, endDate);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetOrdersExcludingProductAndDateBetween_wrongDates() {
        Long productCode = 0L;
        LocalDate startDate = order.getOrderDate().plusDays(2);
        LocalDate endDate = order.getOrderDate().plusDays(5);
        when(ordersRepository.findExcludingProductAndDateBetween(productCode, startDate, endDate)).thenReturn(Collections.emptyList());

        List<OrderResponseDto> result = ordersService.getOrdersExcludingProductAndDateBetween(productCode, startDate, endDate);

        verify(ordersRepository).findExcludingProductAndDateBetween(productCode, startDate, endDate);
        assertNotNull(result);
        assertEquals(0, result.size());
    }
}
