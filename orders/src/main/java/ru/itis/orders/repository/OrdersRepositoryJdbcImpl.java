package ru.itis.orders.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.itis.orders.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class OrdersRepositoryJdbcImpl implements OrdersRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrdersRepositoryJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long save(Order order) {
        Map<String, Object> paramsAsMap = new HashMap<>();

        paramsAsMap.put("order_number", order.getOrderNumber());
        paramsAsMap.put("total_amount", order.getTotalAmount());
        paramsAsMap.put("order_date", order.getOrderDate());
        paramsAsMap.put("recipient", order.getRecipient());
        paramsAsMap.put("delivery_address", order.getDeliveryAddress());
        paramsAsMap.put("payment_type", order.getPaymentType());
        paramsAsMap.put("delivery_type", order.getDeliveryType());

       return simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource(paramsAsMap)).longValue();
    }

    @Override
    public Optional<Order> findById(Long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, orderRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Order> findByDateAfterAndTotalAmountGreaterThan(LocalDate startDate, BigDecimal minTotalAmount) {
        String sql = "SELECT * FROM orders WHERE order_date >= ? AND total_amount >= ?";
        return jdbcTemplate.query(sql, orderRowMapper, startDate, minTotalAmount);
    }

    @Override
    public List<Order> findExcludingProductAndDateBetween(Long productCode, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT * FROM orders WHERE id NOT IN (SELECT order_id FROM order_details WHERE product_code = ?) AND order_date BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, orderRowMapper, productCode, startDate, endDate);
    }

    private static final RowMapper<Order> orderRowMapper = (row, rowNumber) -> Order.builder()
            .id(row.getLong("id"))
            .orderNumber(row.getString("order_number"))
            .totalAmount(row.getBigDecimal("total_amount"))
            .recipient(row.getString("recipient"))
            .deliveryAddress(row.getString("delivery_address"))
            .paymentType(row.getString("payment_type"))
            .deliveryType(row.getString("delivery_type"))
            .orderDate(row.getDate("order_date").toLocalDate())
            .build();
}
