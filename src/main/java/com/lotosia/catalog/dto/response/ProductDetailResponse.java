package com.lotosia.catalog.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Full product detail view - returned on /products/{id}.
 * Contains all images and category info.
 *
 * @author: nijataghayev
 */
@Builder
public record ProductDetailResponse(
        Long id,
        String sku,
        String name,
        String description,
        BigDecimal price,
        boolean active,
        List<String> images,
        CategoryResponse category) {
}