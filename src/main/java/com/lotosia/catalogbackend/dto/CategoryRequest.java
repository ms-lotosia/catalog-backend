package com.lotosia.catalogbackend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: nijataghayev
 */

@Builder
public record CategoryRequest(
        @NotNull
        @Size(min = 1, max = 50)
        String name,

        String description,
        MultipartFile image,

        @NotNull
        @Size(min = 1, max = 100)
        String slug) {
}
