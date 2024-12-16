package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);


    void addProductToCart(int userId, int productId);

    ShoppingCartItem getCartItemByUserIdAndProductId(int userId, int productId);

    void updateProductQuantity(int userId, int productId, int quantity);
    // add additional method signatures here
}
