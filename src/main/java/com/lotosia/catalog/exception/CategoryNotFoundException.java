package com.lotosia.catalog.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when a Category entity cannot be found by its identifier (id or slug).
 *
 * @author: nijataghayev
 */
public class CategoryNotFoundException extends BaseException {

    private static final long serialVersionUID = 1L;

    public CategoryNotFoundException(Long id) {
        super("Category not found with id: " + id, "CATEGORY_NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    public CategoryNotFoundException(String slug) {
        super("Category not found with slug: " + slug, "CATEGORY_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}