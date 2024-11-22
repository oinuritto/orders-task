package ru.itis.orders.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;
import ru.itis.orders.entity.OrderDetails;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderDetailsRepositoryJdbcImpl implements OrderDetailsRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(List<OrderDetails> orderDetailsList) {
        String sql = "INSERT INTO order_details (product_code, product_name, quantity, unit_price, order_id) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                OrderDetails orderDetails = orderDetailsList.get(i);
                ps.setLong(1, orderDetails.getProductCode());
                ps.setString(2, orderDetails.getProductName());
                ps.setInt(3, orderDetails.getQuantity());
                ps.setBigDecimal(4, orderDetails.getUnitPrice());
                ps.setLong(5, orderDetails.getOrderId());
            }

            @Override
            public int getBatchSize() {
                return orderDetailsList.size();
            }
        });
    }

    @Override
    public List<OrderDetails> findAllByOrderId(Long orderId) {
        String sql = "SELECT * FROM order_details WHERE order_id = ?";
        return jdbcTemplate.query(sql, new OrderDetailsRowMapper(), orderId);
    }

    private static class OrderDetailsRowMapper implements RowMapper<OrderDetails> {
        @Override
        public OrderDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setId(rs.getLong("id"));
            orderDetails.setProductCode(rs.getLong("product_code"));
            orderDetails.setProductName(rs.getString("product_name"));
            orderDetails.setQuantity(rs.getInt("quantity"));
            orderDetails.setUnitPrice(rs.getBigDecimal("unit_price"));
            orderDetails.setOrderId(rs.getLong("order_id"));
            return orderDetails;
        }
    }
}
