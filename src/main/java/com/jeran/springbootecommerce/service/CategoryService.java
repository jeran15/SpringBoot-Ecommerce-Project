package com.jeran.springbootecommerce.service;

import com.jeran.springbootecommerce.model.Category;
import com.jeran.springbootecommerce.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse getAllCategories();

    void createCategory(Category category);

    String deleteCategory(Long categoryId);

    Category updateCategory(Category category, Long categoryId);
}