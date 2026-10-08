package com.lotosia.catalog.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: nijataghayev
 */

@Builder
public record ProductRequest(

        @NotNull
        @Size(min = 1, max = 255)
        String name,
        String description,
        String sku,

        @NotNull
        @Positive
        Double price,

        MultipartFile[] images,

        @NotNull
        Long categoryId) {
}
