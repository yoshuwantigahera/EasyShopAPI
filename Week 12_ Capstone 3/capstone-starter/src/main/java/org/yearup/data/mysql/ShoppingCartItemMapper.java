package org.yearup.data.mysql;


import org.springframework.jdbc.core.RowMapper;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCartItem;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShoppingCartItemMapper implements RowMapper<ShoppingCartItem> {

    @Override
    public ShoppingCartItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Map the Product
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getBigDecimal("price"));

        // Map the ShoppingCartItem
        ShoppingCartItem item = new ShoppingCartItem();
        item.setProduct(product);  // Set the mapped product
        item.setQuantity(rs.getInt("quantity"));
        item.setDiscountPercent(rs.getBigDecimal("discount_percent")); // Ensure your database has this column

        return item;
    }
}
