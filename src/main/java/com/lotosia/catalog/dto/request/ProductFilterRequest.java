package com.lotosia.catalog.dto.request;

import com.lotosia.catalog.enums.ProductSortType;
import lombok.Builder;

import java.math.BigDecimal;

/**
 * @author: nijataghayev
 */

@Builder
public record ProductFilterRequest(
        String keyword,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Long categoryId,
        ProductSortType sortBy,
        Integer page,
        Integer size) {

    public ProductFilterRequest {
        if (page == null || page < 0) page = 0;
        if (size == null || size <= 0) size = 12;
        if (sortBy == null) sortBy = ProductSortType.NEWEST;
    }
}

