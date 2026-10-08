package com.lotosia.catalog.controller.publicapi;

import com.lotosia.catalog.dto.ApiResponse;
import com.lotosia.catalog.dto.request.ProductFilterRequest;
import com.lotosia.catalog.dto.response.ProductDetailResponse;
import com.lotosia.catalog.dto.response.ProductResponse;
import com.lotosia.catalog.enums.ProductSortType;
import com.lotosia.catalog.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * Public (unauthenticated) product endpoints for the storefront.
 *
 * @author: nijataghayev
 */
@Tag(name = "Products (Public)", description = "Public product listing, filtering and detail endpoints")
@RestController
@RequestMapping("/api/v1/public/products")
@RequiredArgsConstructor
public class ProductPublicController {

    private final ProductService productService;

    @Operation(summary = "Filter and list products")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> filterProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) ProductSortType sortBy,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "12") Integer size) {

        ProductFilterRequest request = ProductFilterRequest.builder()
                .keyword(keyword)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .categoryId(categoryId)
                .sortBy(sortBy)
                .page(page)
                .size(size)
                .build();

        return ResponseEntity.ok(ApiResponse.success(productService.filterProducts(request)));
    }

    @Operation(summary = "Get product detail by id")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProductById(id)));
    }

    @Operation(summary = "Get latest products")
    @GetMapping("/latest")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getLatestProducts(
            @RequestParam(defaultValue = "8") int limit) {
        return ResponseEntity.ok(ApiResponse.success(productService.getLatestProducts(limit)));
    }

    @Operation(summary = "Get random products for the 'For You' section")
    @GetMapping("/for-you")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getForYouProducts(
            @RequestParam(defaultValue = "6") int limit) {
        return ResponseEntity.ok(ApiResponse.success(productService.getForYouProducts(limit)));
    }
}