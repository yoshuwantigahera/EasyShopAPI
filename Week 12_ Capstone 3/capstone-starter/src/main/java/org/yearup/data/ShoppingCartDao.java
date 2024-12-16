package org.yearup.data;

import org.springframework.stereotype.Repository;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
@Repository
public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);


    void addProductToCart(int userId, int productId);

    ShoppingCartItem getCartItemByUserIdAndProductId(int userId, int productId);

    void updateProductQuantity(int userId, int productId, int quantity);

    void clearCartByUserId(int userId);

    // add additional method signatures here
}
