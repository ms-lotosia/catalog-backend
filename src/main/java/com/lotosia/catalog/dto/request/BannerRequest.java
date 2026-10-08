package com.lotosia.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * Request DTO for creating or updating a Banner.
 *
 * @author: nijataghayev
 */
@Builder
public record BannerRequest(

        @NotBlank(message = "{validation.banner.title.required}")
        @Size(min = 1, max = 255, message = "{validation.banner.title.size}")
        String title,

        @Size(max = 1000, message = "{validation.banner.description.size}")
        String description,

        @NotBlank(message = "{validation.banner.imageUrl.required}")
        @Size(max = 500, message = "{validation.banner.imageUrl.size}")
        String imageUrl,

        boolean active) {
}