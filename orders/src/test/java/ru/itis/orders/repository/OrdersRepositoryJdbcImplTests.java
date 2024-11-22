package ru.itis.orders.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.util.ReflectionTestUtils;
import ru.itis.orders.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrdersRepositoryJdbcImplTests {
    @Mock
    private JdbcTemplate jdbcTemplate;
    @InjectMocks
    private OrdersRepositoryJdbcImpl ordersRepository;

    private Order order;

    @BeforeEach
    void setUp() {
        order = Order.builder()
                .id(1L)
                .orderNumber("1111120241111")
                .totalAmount(BigDecimal.valueOf(100.0))
                .orderDate(LocalDate.now())
                .recipient("Test Recipient")
                .deliveryAddress("Test Address")
                .paymentType("Card")
                .deliveryType("Courier")
                .build();
    }

    @Test
    void testSave() {
        SimpleJdbcInsert simpleJdbcInsert = mock(SimpleJdbcInsert.class);
        ReflectionTestUtils.setField(ordersRepository, "simpleJdbcInsert", simpleJdbcInsert);
        when(simpleJdbcInsert.executeAndReturnKey(any(MapSqlParameterSource.class))).thenReturn(order.getId());

        var result = ordersRepository.save(order);

        assertEquals(order.getId(), result);
        verify(simpleJdbcInsert).executeAndReturnKey(any(MapSqlParameterSource.class));
    }

    @Test
    void testFindById() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(order.getId()))).thenReturn(order);

        var result = ordersRepository.findById(order.getId());

        assertTrue(result.isPresent());
    }

    @Test
    void testFindByIdNotFound() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(order.getId())))
                .thenThrow(new EmptyResultDataAccessException(1));

        var foundOrder = ordersRepository.findById(1L);

        assertFalse(foundOrder.isPresent());
    }

    @Test
    void testFindByDateAfterAndTotalAmountGreaterThan() {
        var date = order.getOrderDate().minusDays(1);
        var totalAmount = order.getTotalAmount().subtract(BigDecimal.valueOf(1));

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(date), eq(totalAmount)))
                .thenReturn(Collections.singletonList(order));

        var orders = ordersRepository.findByDateAfterAndTotalAmountGreaterThan(date, totalAmount);

        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(order.getId(), orders.get(0).getId());
    }

    @Test
    void testFindExcludingProductAndDateBetween() {
        var date1 = order.getOrderDate().minusDays(1);
        var date2 = order.getOrderDate().plusDays(1);
        var productCode = 100L;

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(productCode), eq(date1), eq(date2)))
                .thenReturn(Collections.singletonList(order));

        var orders = ordersRepository.findExcludingProductAndDateBetween(productCode, date1, date2);

        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(order.getId(), orders.get(0).getId());
    }
}

