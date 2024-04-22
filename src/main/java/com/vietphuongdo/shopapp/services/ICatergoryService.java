package com.vietphuongdo.shopapp.services;

import com.vietphuongdo.shopapp.dtos.CategoryDTO;
import com.vietphuongdo.shopapp.entities.Category;

import java.util.List;

public interface ICatergoryService {
    Category createCategory(CategoryDTO categoryDTO);
    Category getCategoryById(Long categoryId);
    List<Category> getAllCategories();
    Category updateCategory(Long categoryId, CategoryDTO categoryDTO);
    void deleteCategory(Long categoryId);
}
