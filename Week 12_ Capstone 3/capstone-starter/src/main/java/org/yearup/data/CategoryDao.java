package org.yearup.data;

import org.yearup.controllers.CategoriesController;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;
import java.util.stream.Collectors;

public interface CategoryDao
{
    List<Category> getAllCategories();
    Category getById(int categoryId);
    Category create(Category category);
    void update(int categoryId, Category category);
    void delete(int categoryId);

}
