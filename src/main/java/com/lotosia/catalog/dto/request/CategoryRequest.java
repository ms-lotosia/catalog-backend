package com.lotosia.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: nijataghayev
 */

@Builder
public record CategoryRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 1, max = 50)
        String name,
        String description,
        MultipartFile image) {
}
