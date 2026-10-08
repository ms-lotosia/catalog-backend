package com.lotosia.catalog.dto.response;

import jakarta.persistence.Basic;
import lombok.Builder;

/**
 * @author: nijataghayev
 */

@Builder
public record CategoryResponse(
        Long id,
        String name,
        String description,
        String image,
        String slug) {
}

