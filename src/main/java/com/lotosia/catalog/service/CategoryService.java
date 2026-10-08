package com.lotosia.catalog.service;

import com.lotosia.catalog.dto.request.CategoryRequest;
import com.lotosia.catalog.dto.response.CategoryResponse;

import java.util.List;

/**
 * @author: nijataghayev
 */

public interface CategoryService {

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(Long id);

    CategoryResponse getCategoryBySlug(String slug);

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    void deleteCategory(Long id);
}