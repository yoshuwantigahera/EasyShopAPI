package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.*;

import java.security.Principal;
import java.util.NoSuchElementException;

// convert this class to a REST controller
// only logged in users should have access to these actions
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartDao shoppingCartDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    // GET: Fetch the shopping cart for the current user
    @GetMapping
    public ResponseEntity<ShoppingCart> getCart(Principal principal) {
        try {
            User user = getAuthenticatedUser(principal);
            ShoppingCart cart = shoppingCartDao.getByUserId(user.getId());

            if (cart == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.ok(cart);
        } catch (NoSuchElementException e) { // Use this if you don't have UserNotFoundException
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // POST: Add a product to the user's cart
    @PostMapping("/products/{productId}")
    public ResponseEntity<Product> addProductToCart(@PathVariable int productId, Principal principal) {
        try {
            // Get authenticated user
            User user = getAuthenticatedUser(principal);

            // Get the product by its ID
            Product product = productDao.getById(productId);

            // Check if the product exists
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // No body, just status
            }

            // Add the product to the user's shopping cart
            shoppingCartDao.addProductToCart(user.getId(), productId);

            // Return the product as a response
            return ResponseEntity.ok(product);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // PUT: Update product quantity in the cart
    @PutMapping("/products/{productId}")
    public ResponseEntity<?> updateProductInCart(
            @PathVariable int productId,
            @RequestBody ShoppingCartItem updatedItem,
            Principal principal) {
        try {
            User user = getAuthenticatedUser(principal);
            ShoppingCartItem existingItem = shoppingCartDao.getCartItemByUserIdAndProductId(user.getId(), productId);

            if (existingItem == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found in cart");
            }

            if (updatedItem.getQuantity() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantity must be greater than zero");
            }

            shoppingCartDao.updateProductQuantity(user.getId(), productId, updatedItem.getQuantity());
            return ResponseEntity.ok("Product quantity updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the product in the cart");
        }
    }

    // DELETE: Clear all products from the user's cart
    @DeleteMapping
    public ResponseEntity<?> clearCart(Principal principal) {
        try {
            User user = getAuthenticatedUser(principal);
            shoppingCartDao.clearCartByUserId(user.getId());
            return ResponseEntity.ok("All products cleared from cart");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while clearing the cart");
        }
    }

    // Helper method to retrieve the authenticated user
    private User getAuthenticatedUser(Principal principal) {
        String userName = principal.getName();
        User user = userDao.getByUserName(userName);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return user;
    }
}


