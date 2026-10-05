package com.lotosia.catalogbackend.entity;

import jakarta.persistence.Basic;
import lombok.Builder;

/**
 * @author: nijataghayev
 */

@Builder
public record CategoryResponse(
        @Basic(optional = false)
        String name,

        String description,
        String image,

        @Basic(optional = false)
        String slug) {
}
