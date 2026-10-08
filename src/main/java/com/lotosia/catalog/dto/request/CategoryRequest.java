package com.lotosia.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * Request DTO for creating or updating a Category.
 * Image is stored externally (e.g. GCS / MinIO); the caller provides the resolved URL.
 *
 * @author: nijataghayev
 */
@Builder
public record CategoryRequest(

        @NotBlank(message = "{validation.category.name.required}")
        @Size(min = 1, max = 50, message = "{validation.category.name.size}")
        String name,

        @Size(max = 500, message = "{validation.category.description.size}")
        String description,

        String imageUrl) {
}