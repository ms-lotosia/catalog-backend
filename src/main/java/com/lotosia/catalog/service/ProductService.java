package com.lotosia.catalog.service;

import com.lotosia.catalog.dto.request.ProductFilterRequest;
import com.lotosia.catalog.dto.request.ProductRequest;
import com.lotosia.catalog.dto.request.ProductUpdateRequest;
import com.lotosia.catalog.dto.response.ProductDetailResponse;
import com.lotosia.catalog.dto.response.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author: nijataghayev
 */

public interface ProductService {

    Page<ProductResponse> filterProducts(ProductFilterRequest request);

    ProductDetailResponse getProductById(Long id);

    List<ProductResponse> getLatestProducts(int limit);

    List<ProductResponse> getForYouProducts(int limit);

    ProductResponse createProduct(ProductRequest request);

    ProductResponse updateProduct(Long id, ProductUpdateRequest request);

    ProductResponse setActive(Long id, boolean active);

    void deleteProduct(Long id);
}