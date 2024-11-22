package ru.itis.orders.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.itis.orders.entity.OrderDetails;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderDetailsRepositoryJdbcImplTests {
    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private OrderDetailsRepositoryJdbcImpl orderDetailsRepository;

    private OrderDetails orderDetails;

    @BeforeEach
    void setUp() {
        orderDetails = new OrderDetails();
        orderDetails.setId(1L);
        orderDetails.setProductCode(100L);
        orderDetails.setProductName("Product Test");
        orderDetails.setQuantity(2);
        orderDetails.setUnitPrice(BigDecimal.valueOf(20.0));
        orderDetails.setOrderId(1L);
    }

    @Test
    void testSave() {
        when(jdbcTemplate.batchUpdate(anyString(), any(BatchPreparedStatementSetter.class))).thenReturn(new int[]{1});

        orderDetailsRepository.save(Collections.singletonList(orderDetails));

        verify(jdbcTemplate).batchUpdate(anyString(), any(BatchPreparedStatementSetter.class));
    }

    @Test
    void testFindAllByOrderId() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(orderDetails.getOrderId())))
                .thenReturn(Collections.singletonList(orderDetails));

        var result = orderDetailsRepository.findAllByOrderId(orderDetails.getOrderId());

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindAllByOrderIdNotFound() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(orderDetails.getOrderId())))
                .thenReturn(Collections.emptyList());

        var result = orderDetailsRepository.findAllByOrderId(orderDetails.getOrderId());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
