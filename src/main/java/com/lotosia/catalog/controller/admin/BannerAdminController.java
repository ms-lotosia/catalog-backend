package com.lotosia.catalog.controller.admin;

import com.lotosia.catalog.dto.ApiResponse;
import com.lotosia.catalog.dto.request.BannerRequest;
import com.lotosia.catalog.dto.response.BannerResponse;
import com.lotosia.catalog.service.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

import java.util.List;

/**
 * Admin-only banner management endpoints.
 * All endpoints require the ADMIN role.
 *
 * @author: nijataghayev
 */
@Tag(name = "Banners (Admin)", description = "Admin CRUD operations for banners")
@RestController
@RequestMapping("/api/v1/admin/banners")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class BannerAdminController {

    private final BannerService bannerService;

    @Operation(summary = "List all banners (active and inactive)")
    @GetMapping
    public ResponseEntity<ApiResponse<List<BannerResponse>>> getAllBanners() {
        return ResponseEntity.ok(ApiResponse.success(bannerService.getAllBanners()));
    }

    @Operation(summary = "Get banner by id")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BannerResponse>> getBannerById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(bannerService.getBannerById(id)));
    }

    @Operation(summary = "Create a new banner")
    @PostMapping
    public ResponseEntity<ApiResponse<BannerResponse>> createBanner(
            @Valid @RequestBody BannerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(bannerService.createBanner(request)));
    }

    @Operation(summary = "Update an existing banner (full replace)")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BannerResponse>> updateBanner(
            @PathVariable Long id,
            @Valid @RequestBody BannerRequest request) {
        return ResponseEntity.ok(ApiResponse.success(bannerService.updateBanner(id, request)));
    }

    @Operation(summary = "Toggle banner visibility (active/inactive)")
    @PatchMapping("/{id}/active")
    public ResponseEntity<ApiResponse<BannerResponse>> setActive(
            @PathVariable Long id,
            @RequestParam boolean active) {
        return ResponseEntity.ok(ApiResponse.success(bannerService.setActive(id, active)));
    }

    @Operation(summary = "Delete a banner permanently")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long id) {
        bannerService.deleteBanner(id);
        return ResponseEntity.noContent().build();
    }
}