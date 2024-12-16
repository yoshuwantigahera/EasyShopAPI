package org.yearup.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@CrossOrigin
@PreAuthorize("permitAll()")
@RequestMapping("/categories")

public class CategoriesController {

    private final CategoryDao categoryDao;
    private final ProductDao productDao;

    // Constructor-based Dependency Injection
    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    // GET method to retrieve all categories
    @GetMapping
    public List<Category> getAll() {
        return categoryDao.getAllCategories();
    }

     //add the appropriate annotation for a get action
    @GetMapping("{categoryID}")
    public ResponseEntity<Category> getById(@PathVariable int categoryID) {
        Category category = categoryDao.getById(categoryID);
        if(category != null){
            return ResponseEntity.ok(category);

        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

//Green
//     the url to return all products in category 1 would look like this
//     https://localhost:8080/categories/1/products
@GetMapping("/{categoryId}/products")
public ResponseEntity<List<Product>> getProductsByCategoryId(@PathVariable int categoryId) {
    // Retrieve products by categoryId
    List<Product> products = productDao.listByCategoryId(categoryId);

    if (products == null || products.isEmpty()) {
        // Return 404 if no products found for the category
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Return the list of products with 200 OK
    return ResponseEntity.ok(products);
}

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Category addCategory(@RequestBody Category category) {
            // Use the DAO to create the category
             category = categoryDao.create(category);
             return category;
        }

//    @PostMapping("")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public ResponseEntity<?> addCategory(@RequestBody Category category) {
//        try {
//            // Validate the input (basic example)
//            if (category == null || category.getName() == null || category.getName().isEmpty()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category name is required.");
//            }
//
//            // Use the DAO to create the category
//            Category createdCategory = categoryDao.create(category);
//
//            if (createdCategory != null) {
//                return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
//            } else {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add category");
//            }
//        } catch (IllegalArgumentException e) {
//            // Handle specific validation or argument errors
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid category details: " + e.getMessage());
//        } catch (DataAccessException e) {
//            // Handle database-related errors (e.g., using Spring's DataAccessException)
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error: " + e.getMessage());
//        } catch (Exception e) {
//            // Catch any other unexpected errors
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
//        }
//    }



//    // add annotation to call this method for a POST action
//    // add annotation to ensure that only an ADMIN can call this function
//    @PostMapping("")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public ResponseEntity<String> addCategory(@RequestBody Category category) {
//        try {
//            if (category == null || category.getName() == null || category.getName().isEmpty()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category name is required.");
//            // Use the DAO to create the category
//            Category createdCategory = categoryDao.create(category);
//
//            if (createdCategory != null) {
//                return ResponseEntity.status(HttpStatus.CREATED).body("Category added successfully");
//            } else {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add category");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
//        }
//    }



    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
        @PutMapping("/{id}")
        @PreAuthorize("hasRole('ROLE_ADMIN')")
        public ResponseEntity<?> updateCategory(@RequestBody Category category) {
            try {
                int categoryId = category.getCategoryId(); // Assuming the Category object has a getId() method to get the ID.
                categoryDao.update(categoryId, category); // Update the category with its ID and the new data.

                return ResponseEntity.ok("Category updated successfully");
            } catch (Exception e) {
                // Log the exception (optional, depending on your logging setup)
                // logger.error("Error updating category", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the category");
            }
        }




    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @DeleteMapping("/categories/{categoryId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteCategory(@PathVariable int categoryId) {
        try {
            Category existingCategory = categoryDao.getById(categoryId); // Check if the category exists

            if (existingCategory == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
            }

            categoryDao.delete(categoryId);  // Delete the category if it exists
            return ResponseEntity.ok("Category deleted successfully");
        } catch (Exception e) {
            // Log the exception (optional)
            // logger.error("Error deleting category", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the category");
        }
    }

        }
