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


//    // each method in this controller requires a Principal object as a parameter
//    public ShoppingCart getCart(Principal principal) {
//        try {
//            // Get the currently logged-in username
//            String userName = principal.getName();
//
//            // Find the user in the database by username
//            User user = userDao.getByUserName(userName);
//            if (user == null) {
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
//            }
//
//            int userId = user.getId();
//
//            // Use the shoppingCartDao to get all items in the user's cart
//            ShoppingCart cart = shoppingCartDao.getByUserId(userId);
//            if (cart == null) {
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shopping cart not found");
//            }
//
//            return cart;
//        } catch (ResponseStatusException e) {
//            // Rethrow expected exceptions
//            throw e;
//        } catch (Exception e) {
//            // Catch unexpected exceptions and return a 500 error
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while fetching the shopping cart");
//        }
//    }
//
//
//    // add a POST method to add a product to the cart - the url should be
//    // https://localhost:8080/cart/products/15 (15 is the productId to be added
//    @PostMapping("/cart/products/{productId}")
//    public ResponseEntity<?> addProductToCart(@PathVariable int productId, Principal principal) {
//        try {
//            // Get the currently logged-in username
//            String userName = principal.getName();
//
//            // Find the user in the database by username
//            User user = userDao.getByUserName(userName);
//            if (user == null) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//            }
//
//            int userId = user.getId();
//
//            // Verify that the product exists
//            Product product = productDao.getById(productId); // Assumes a method in productDao to get a product by ID
//            if (product == null) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
//            }
//
//            // Add the product to the user's cart
//            shoppingCartDao.addProductToCart(userId, productId);
//
//            return ResponseEntity.ok("Product added to cart successfully");
//        } catch (Exception e) {
//            // Log the exception (optional)
//            // logger.error("Error adding product to cart", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding the product to the cart");
//        }
//    }
//
//
//    // add a PUT method to update an existing product in the cart - the url should be
//    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
//    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated
//    @PutMapping("/cart/products/{productId}")
//    public ResponseEntity<?> updateProductInCart(@PathVariable int productId, @RequestBody ShoppingCartItem updatedItem, Principal principal) {
//        try {
//            String userName = principal.getName();   // Get  currently loggedin username
//
//
//            User user = userDao.getByUserName(userName); // Find the user in the database by username
//
//            if (user == null) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//            }
//
//            int userId = user.getId();
//
//            // to verify  the product exists in the user's cart
//            ShoppingCartItem existingItem = shoppingCartDao.getCartItemByUserIdAndProductId(userId, productId);
//            if (existingItem == null) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found in cart");
//            }
//
//            //  to update the quantity of the product in the cart
//            shoppingCartDao.updateProductQuantity(userId, productId, updatedItem.getQuantity());
//
//            return ResponseEntity.ok("Product quantity updated successfully");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the product in the cart");
//        }
//    }
//
//
//    // add a DELETE method to clear all products from the current users cart
//    // https://localhost:8080/cart
//    @DeleteMapping("/cart")
//}

