package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

// convert this class to a REST controller
// only logged in users should have access to these actions
@RestController
@PreAuthorize("hasRole('ROLE_USER')")

public class ShoppingCartController {
    // a shopping cart requires
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;



    // GET: Fetch the shopping cart for the current user
    @GetMapping
    public ShoppingCart getCart(Principal principal) {
        try {
            User user = getAuthenticatedUser(principal);
            ShoppingCart cart = shoppingCartDao.getByUserId(user.getId());
            if (cart == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shopping cart not found");
            }
            return cart;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while fetching the shopping cart");
        }
    }

    // POST: Add a product to the user's cart
    @PostMapping("/products/{productId}")
    public ResponseEntity<?> addProductToCart(@PathVariable int productId, Principal principal) {
        try {
            User user = getAuthenticatedUser(principal);
            Product product = productDao.getById(productId);
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
            }
            shoppingCartDao.addProductToCart(user.getId(), productId);
            return ResponseEntity.ok("Product added to cart successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding the product to the cart");
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

