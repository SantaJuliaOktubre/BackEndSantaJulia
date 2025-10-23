package com.foodstore.foodstore.service;

import com.foodstore.foodstore.entity.Category;
import java.util.List;

public interface CategoryService {
    List<Category> getAll();
    Category getById(Long id);
    Category create(Category category);
    Category update(Long id, Category category);
    void delete(Long id);
}
