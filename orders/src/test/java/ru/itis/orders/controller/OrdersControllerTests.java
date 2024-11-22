package ru.itis.orders.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.itis.orders.dto.order.OrderRequestDto;
import ru.itis.orders.dto.orderDetails.OrderDetailsRequestDto;
import ru.itis.orders.entity.Order;
import ru.itis.orders.entity.OrderDetails;
import ru.itis.orders.exception.NotFoundException;
import ru.itis.orders.mapper.OrderMapper;
import ru.itis.orders.service.OrdersService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrdersController.class)
public class OrdersControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrdersService ordersService;
    @Autowired
    private ObjectMapper objectMapper;
    private final OrderMapper orderMapper = OrderMapper.INSTANCE;
    private Order order;
    private List<OrderDetails> orderDetailsList;

    @BeforeEach
    void setUp() {
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
                        .id(1L)
                        .productCode(1L)
                        .unitPrice(BigDecimal.valueOf(1.0))
                        .productName("product")
                        .quantity(1)
                        .orderId(order.getId())
                        .build(),
                OrderDetails.builder()
                        .id(2L)
                        .productCode(2L)
                        .unitPrice(BigDecimal.valueOf(2.0))
                        .productName("product2")
                        .quantity(2)
                        .orderId(order.getId())
                        .build());
    }

    @Test
    void createOrder_ShouldReturn201() throws Exception {
        var id = order.getId();
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

        when(ordersService.createOrder(any(OrderRequestDto.class))).thenReturn(id);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string(id.toString()));

        verify(ordersService).createOrder(any(OrderRequestDto.class));
    }

    @Test
    void createOrder_ShouldReturn400() throws Exception {
        var orderRequestDto = OrderRequestDto.builder().deliveryType("delivery").build();

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOrderById_ShouldReturn200() throws Exception {
        var id = order.getId();
        when(ordersService.getOrder(id))
                .thenReturn(orderMapper.toOrderWithDetailsResponseDto(order, orderMapper.toOrderDetailsResponseDtoList(orderDetailsList)));

        mockMvc.perform(get("/orders/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.items", hasSize(orderDetailsList.size())));

        verify(ordersService).getOrder(id);
    }

    @Test
    void getOrderById_ShouldReturn404() throws Exception {
        doThrow(new NotFoundException("not found")).when(ordersService).getOrder(anyLong());

        mockMvc.perform(get("/orders/{id}", 1L))
                .andExpect(status().isNotFound());

        verify(ordersService).getOrder(anyLong());
    }

    @Test
    void getOrdersByDateAfterAndTotalAmountGreaterThan_ShouldReturn200() throws Exception {
        when(ordersService.getOrdersByDateAfterAndTotalAmountGreaterThan(any(LocalDate.class), any(BigDecimal.class)))
                .thenReturn(orderMapper.toOrderResponseDtoList(List.of(order)));

        mockMvc.perform(get("/orders/search")
                        .param("startDate", String.valueOf(order.getOrderDate().minusDays(1)))
                        .param("minTotalAmount", String.valueOf(order.getTotalAmount().subtract(BigDecimal.valueOf(1)))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(order.getId().intValue())));

        verify(ordersService).getOrdersByDateAfterAndTotalAmountGreaterThan(any(LocalDate.class), any(BigDecimal.class));
    }

    @Test
    void getOrdersExcludingProduct_ShouldReturn200() throws Exception {
        when(ordersService.getOrdersExcludingProductAndDateBetween(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(orderMapper.toOrderResponseDtoList(List.of(order)));

        mockMvc.perform(get("/orders/exclude-product")
                        .param("productCode", "100")
                        .param("startDate", "2020-01-01")
                        .param("endDate", "2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(order.getId().intValue())));

        verify(ordersService).getOrdersExcludingProductAndDateBetween(anyLong(), any(LocalDate.class), any(LocalDate.class));
    }
}

