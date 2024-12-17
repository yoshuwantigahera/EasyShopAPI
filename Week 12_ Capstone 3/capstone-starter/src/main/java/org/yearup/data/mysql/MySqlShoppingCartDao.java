package org.yearup.data.mysql;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import java.util.List;

@Repository
public class MySqlShoppingCartDao implements ShoppingCartDao {

    private final JdbcTemplate jdbcTemplate;

    public MySqlShoppingCartDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        String sql = "SELECT ci.product_id, ci.quantity, p.name, p.price " +
                "FROM shopping_cart c " +
                "JOIN cart_items ci ON c.id = ci.cart_id " +
                "JOIN products p ON ci.product_id = p.id " +
                "WHERE c.user_id = ?";

        List<ShoppingCartItem> items = jdbcTemplate.query(sql, new Object[]{userId}, new ShoppingCartItemMapper());

        ShoppingCart cart = new ShoppingCart();
        for (ShoppingCartItem item : items) {
            cart.add(item); // Populates the `items` map in the ShoppingCart
        }

        return cart;
    }

    @Override
    public void addProductToCart(int userId, int productId) {
        String findCartSql = "SELECT id FROM shopping_cart WHERE user_id = ?";
        Integer cartId = jdbcTemplate.queryForObject(findCartSql, new Object[]{userId}, Integer.class);

        if (cartId == null) {
            String createCartSql = "INSERT INTO shopping_cart (user_id) VALUES (?)";
            jdbcTemplate.update(createCartSql, userId);
            cartId = jdbcTemplate.queryForObject(findCartSql, new Object[]{userId}, Integer.class);
        }

        String checkProductSql = "SELECT COUNT(*) FROM cart_items WHERE cart_id = ? AND product_id = ?";
        int count = jdbcTemplate.queryForObject(checkProductSql, new Object[]{cartId, productId}, Integer.class);

        if (count > 0) {
            String updateSql = "UPDATE cart_items SET quantity = quantity + 1 WHERE cart_id = ? AND product_id = ?";
            jdbcTemplate.update(updateSql, cartId, productId);
        } else {
            String insertSql = "INSERT INTO cart_items (cart_id, product_id, quantity) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertSql, cartId, productId, 1);
        }
    }

    @Override
    public ShoppingCartItem getCartItemByUserIdAndProductId(int userId, int productId) {
        return null;
    }

    @Override
    public void updateProductQuantity(int userId, int productId, int quantity) {
        String findCartSql = "SELECT id FROM shopping_cart WHERE user_id = ?";
        Integer cartId = jdbcTemplate.queryForObject(findCartSql, new Object[]{userId}, Integer.class);

        if (cartId != null) {
            String updateSql = "UPDATE cart_items SET quantity = ? WHERE cart_id = ? AND product_id = ?";
            jdbcTemplate.update(updateSql, quantity, cartId, productId);
        }
    }

    @Override
    public void clearCartByUserId(int userId) {
        String findCartSql = "SELECT id FROM shopping_cart WHERE user_id = ?";
        Integer cartId = jdbcTemplate.queryForObject(findCartSql, new Object[]{userId}, Integer.class);

        if (cartId != null) {
            String deleteItemsSql = "DELETE FROM cart_items WHERE cart_id = ?";
            jdbcTemplate.update(deleteItemsSql, cartId);
        }
    }
}
