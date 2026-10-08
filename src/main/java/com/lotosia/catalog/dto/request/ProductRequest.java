package com.lotosia.catalog.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Request DTO for creating a new Product.
 * Images are stored externally; caller provides resolved URLs.
 *
 * @author: nijataghayev
 */
@Builder
public record ProductRequest(

        @NotBlank(message = "{validation.product.name.required}")
        @Size(min = 1, max = 200, message = "{validation.product.name.size}")
        String name,

        @Size(max = 2000, message = "{validation.product.description.size}")
        String description,

        @NotBlank(message = "{validation.product.sku.required}")
        @Size(min = 1, max = 64, message = "{validation.product.sku.size}")
        String sku,

        @NotNull(message = "{validation.product.price.required}")
        @Positive(message = "{validation.product.price.positive}")
        @Digits(integer = 10, fraction = 2, message = "{validation.product.price.digits}")
        BigDecimal price,

        List<String> imageUrls,

        @NotNull(message = "{validation.product.category.required}")
        Long categoryId) {
}