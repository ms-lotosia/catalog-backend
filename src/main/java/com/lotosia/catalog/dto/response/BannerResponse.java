package com.lotosia.catalog.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Public-facing projection of a Banner.
 *
 * @author: nijataghayev
 */
@Builder
public record BannerResponse(
        Long id,
        String title,
        String description,
        String imageUrl,
        boolean active,
        LocalDateTime createdDate) {
}