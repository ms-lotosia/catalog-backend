package com.lotosia.catalog.dto.response;

import lombok.Builder;

/**
 * @author: nijataghayev
 */

@Builder
public record ProductResponse(
        Long id,
        String name,
        String description,
        String sku,
        Double price,
        String[] images,
        Long categoryId) {
}
