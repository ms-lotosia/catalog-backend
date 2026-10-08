package com.lotosia.catalog.specification;

import com.lotosia.catalog.dto.request.ProductFilterRequest;
import com.lotosia.catalog.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * JPA Criteria-based specifications for filtering {@link Product} entities.
 *
 * All conditions are combined with AND.
 * keyword  - case-insensitive LIKE on name OR description
 * price    - inclusive range
 * category - exact match on the category FK
 * Only active = true products are included.
 *
 * @author: nijataghayev
 */
public final class ProductSpecification {

    private ProductSpecification() {}

    public static Specification<Product> from(ProductFilterRequest filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isTrue(root.get("active")));

            if (filter.keyword() != null && !filter.keyword().isBlank()) {
                String pattern = "%" + filter.keyword().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), pattern),
                        cb.like(cb.lower(root.get("description")), pattern)
                ));
            }

            if (filter.minPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), filter.minPrice()));
            }

            if (filter.maxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), filter.maxPrice()));
            }

            if (filter.categoryId() != null) {
                predicates.add(cb.equal(root.get("category").get("id"), filter.categoryId()));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}