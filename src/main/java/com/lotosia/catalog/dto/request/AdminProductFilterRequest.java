package com.lotosia.catalog.dto.request;

import com.lotosia.catalog.enums.ProductSortType;
import lombok.Builder;

import java.math.BigDecimal;

/**
 * @author: nijataghayev
 * Admin version of the product filter — allows filtering by active status (including inactive products).
 */

@Builder
public record AdminProductFilterRequest(
        String keyword,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Long categoryId,
        Boolean active,
        ProductSortType sortBy,
        Integer page,
        Integer size) {

    public AdminProductFilterRequest {
        if (page == null || page < 0) page = 0;
        if (size == null || size <= 0) size = 20;
        if (sortBy == null) sortBy = ProductSortType.NEWEST;
    }
}
