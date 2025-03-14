package com.jeran.springbootecommerce.service;


import com.jeran.springbootecommerce.payload.CategoryDTO;
import com.jeran.springbootecommerce.payload.CategoryResponse;



public interface CategoryService {

    CategoryResponse getAllCategories();

    CategoryDTO createCategory(CategoryDTO category);

    CategoryDTO deleteCategory(Long categoryId);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);
}