package com.lotosia.catalog.dto.response;

import lombok.Builder;

/**
 * Public-facing projection of a Category.
 *
 * @author: nijataghayev
 */
@Builder
public record CategoryResponse(
        Long id,
        String name,
        String description,
        String imageUrl,
        String slug,
        int productCount) {
}