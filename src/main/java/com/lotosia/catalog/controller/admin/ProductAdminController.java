package com.lotosia.catalog.controller.admin;

import com.lotosia.catalog.dto.ApiResponse;
import com.lotosia.catalog.dto.request.AdminProductFilterRequest;
import com.lotosia.catalog.dto.request.ProductRequest;
import com.lotosia.catalog.dto.request.ProductUpdateRequest;
import com.lotosia.catalog.dto.response.ProductDetailResponse;
import com.lotosia.catalog.dto.response.ProductResponse;
import com.lotosia.catalog.enums.ProductSortType;
import com.lotosia.catalog.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * Admin-only product management.
 * Differs from the public API in that:
 *  - Inactive products are visible and filterable.
 *  - GET /{id} resolves regardless of active status.
 *
 * @author: nijataghayev
 */
@Tag(name = "Products (Admin)", description = "Admin CRUD operations for products")
@RestController
@RequestMapping("/api/v1/admin/products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ProductAdminController {

    private final ProductService productService;

    @Operation(summary = "Bütün məhsulları siyahıla (aktiv + qeyri-aktiv, filterlə + axtarışla)")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> listProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) ProductSortType sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        AdminProductFilterRequest filter = AdminProductFilterRequest.builder()
                .keyword(keyword)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .categoryId(categoryId)
                .active(active)
                .sortBy(sortBy)
                .page(page)
                .size(size)
                .build();

        return ResponseEntity.ok(ApiResponse.success(productService.filterProductsAdmin(filter)));
    }

    @Operation(summary = "Məhsulu ID ilə əldə et (aktiv olmasa belə)")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProductByIdAdmin(id)));
    }

    @Operation(summary = "Yeni məhsul yarat")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(productService.createProduct(request)));
    }

    @Operation(summary = "Məhsulu yenilə")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(productService.updateProduct(id, request)));
    }

    @Operation(summary = "Məhsulu aktiv/qeyri-aktiv et")
    @PatchMapping("/{id}/active")
    public ResponseEntity<ApiResponse<ProductResponse>> setActive(
            @PathVariable Long id,
            @RequestParam boolean active) {
        return ResponseEntity.ok(ApiResponse.success(productService.setActive(id, active)));
    }

    @Operation(summary = "Məhsulu sil")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}