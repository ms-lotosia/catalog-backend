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
 * Request DTO for updating an existing Product.
 * Partial updates are NOT supported - all fields must be supplied (PUT semantics).
 *
 * @author: nijataghayev
 */
@Builder
public record ProductUpdateRequest(

        @NotBlank(message = "{validation.product.name.required}")
        @Size(min = 1, max = 200, message = "{validation.product.name.size}")
        String name,

        @Size(max = 2000, message = "{validation.product.description.size}")
        String description,

        @NotNull(message = "{validation.product.price.required}")
        @Positive(message = "{validation.product.price.positive}")
        @Digits(integer = 10, fraction = 2, message = "{validation.product.price.digits}")
        BigDecimal price,

        List<String> imageUrls) {
}