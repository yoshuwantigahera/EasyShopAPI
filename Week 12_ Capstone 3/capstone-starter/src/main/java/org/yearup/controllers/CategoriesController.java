package org.yearup.controllers;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import org.yearup.models.Profile;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.authentication.LoginDto;
import org.yearup.models.authentication.LoginResponseDto;
import org.yearup.models.authentication.RegisterUserDto;
import org.yearup.models.User;
import org.yearup.security.jwt.JWTFilter;
import org.yearup.security.jwt.TokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
    @GetMapping("{id}")
    public ResponseEntity<Category> getById(@PathVariable int id) {
        Category category = categoryDao.getById(id);
        if(category != null){
            return ResponseEntity.ok(category);

        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

//    //add the appropriate annotation for a get action
//    public Category getById(@PathVariable int id)
//    {
//        // get the category by id
//        return null;
//    }

//     the url to return all products in category 1 would look like this
//     https://localhost:8080/categories/1/products
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<List<Product>> getProductsByCategoryId(@PathVariable int categoryId) {
        // Retrieve the category by ID
        Category category = categoryDao.getById(categoryId);

        if (category != null) {
            // Retrieve the list of products from the ProductDao using categoryId
            List<Product> products = productDao.listByCategoryId(categoryId);
            return ResponseEntity.ok(products);
        } else {
            // Return 404 Not Found if the category doesn't exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    // add annotation to call this method for a POST action
    // add annotation to ensure that only an ADMIN can call this function
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> addCategory(@RequestBody Category category) {
        try {
            // Use the DAO to create the category
            Category createdCategory = categoryDao.create(category);

            if (createdCategory != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Category added successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add category");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }



    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    public void updateCategory(@PathVariable int id, @RequestBody Category category)
    {
        // update the category by id
    }


    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    public void deleteCategory(@PathVariable int id)
    {
        // delete the category by id
    }
}
