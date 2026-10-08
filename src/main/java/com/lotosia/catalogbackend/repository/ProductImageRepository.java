package com.lotosia.catalogbackend.repository;

import com.lotosia.catalogbackend.dto.MainImage;
import com.lotosia.catalogbackend.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

/**
 * @author: nijataghayev
 */

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    @Query("""
            select new com.lotosia.catalogbackend.dto.MainImage(i.product.id, i.url)
            from ProductImage i
            where i.product.id in :ids and i.position = 0
            """)
    List<MainImage> findMainImages(@Param("ids") Collection<Long> ids);
}
