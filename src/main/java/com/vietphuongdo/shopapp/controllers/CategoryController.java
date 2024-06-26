package com.vietphuongdo.shopapp.controllers;

import com.vietphuongdo.shopapp.components.LocalizationUtils;
import com.vietphuongdo.shopapp.dtos.CategoryDTO;
import com.vietphuongdo.shopapp.entities.Category;
import com.vietphuongdo.shopapp.responses.CategoryResponse;
import com.vietphuongdo.shopapp.responses.UpdateCategoryResponse;
import com.vietphuongdo.shopapp.services.CategoryService;
import com.vietphuongdo.shopapp.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.descriptor.LocalResolver;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result) {
        CategoryResponse categoryResponse = new CategoryResponse();
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            categoryResponse.setMessage(localizationUtils.getLocalizedMessage(MessageKeys.INSERT_CATEGORY_FAILED));
            categoryResponse.setErrors(errorMessages);
            return ResponseEntity.badRequest().body(categoryResponse);
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(categoryResponse);

    }

    //Hiện tất cả các categories
    @GetMapping("") //http://localhost:8088/api/v1/categories?page=1&limit=10
    public ResponseEntity<List<Category>> getAllCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<UpdateCategoryResponse> updateCategory(@PathVariable Long categoryId, @Valid @RequestBody CategoryDTO categoryDTO,  HttpServletRequest request) {
        UpdateCategoryResponse updateCategoryResponse = new UpdateCategoryResponse();
        categoryService.updateCategory(categoryId, categoryDTO);
        updateCategoryResponse.setMessage(localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_CATEGORY_SUCCESSFULLY));
        return ResponseEntity.ok(updateCategoryResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_CATEGORY_SUCCESSFULLY));
    }
}
