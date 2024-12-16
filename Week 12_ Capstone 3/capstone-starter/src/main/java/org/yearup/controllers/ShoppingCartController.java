package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;

import java.security.Principal;

// convert this class to a REST controller
// only logged in users should have access to these actions
@RestController
@PreAuthorize("hasRole('ROLE_USER')")

public class ShoppingCartController
{
    // a shopping cart requires
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;



    // each method in this controller requires a Principal object as a parameter
    public ShoppingCart getCart(Principal principal) {
        try {
            // Get the currently logged-in username
            String userName = principal.getName();

            // Find the user in the database by username
            User user = userDao.getByUserName(userName);
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }

            int userId = user.getId();

            // Use the shoppingCartDao to get all items in the user's cart
            ShoppingCart cart = shoppingCartDao.getByUserId(userId);
            if (cart == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shopping cart not found");
            }

            return cart;
        } catch (ResponseStatusException e) {
            // Rethrow expected exceptions
            throw e;
        } catch (Exception e) {
            // Catch unexpected exceptions and return a 500 error
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while fetching the shopping cart");
        }
    }


    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be added


    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated


    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart

}
