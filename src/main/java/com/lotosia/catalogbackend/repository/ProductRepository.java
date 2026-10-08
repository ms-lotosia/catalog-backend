package com.lotosia.catalogbackend.repository;

import com.lotosia.catalogbackend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author: nijataghayev
 */

@Repository
public interface ProductRepository
        extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Override
    @EntityGraph(attributePaths = "category")
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"category", "images"})
    Optional<Product> findWithDetailsById(Long id);

    @EntityGraph(attributePaths = {"category", "images"})
    Optional<Product> findWithDetailsByIdAndActiveTrue(Long id);

    @EntityGraph(attributePaths = {"category", "images"})
    Optional<Product> findWithDetailsBySku(String sku);

    boolean existsBySku(String sku);

    @EntityGraph(attributePaths = "category")
    List<Product> findByActiveTrueOrderByCreatedAtDescIdDesc(Pageable pageable);

    @EntityGraph(attributePaths = "category")
    List<Product> findByIdIn(Collection<Long> ids);

    @Query(value = "select id from product where active order by random() limit :limit",
            nativeQuery = true)
    List<Long> findRandomIds(@Param("limit") int limit);
}
