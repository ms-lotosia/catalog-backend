package com.lotosia.catalog.controller.publicapi;

import com.lotosia.catalog.dto.ApiResponse;
import com.lotosia.catalog.dto.response.BannerResponse;
import com.lotosia.catalog.service.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: nijataghayev
 */

@Tag(name = "Banners (Public)", description = "Public active-banner endpoint for the storefront")
@RestController
@RequestMapping("/api/v1/public/banners")
@RequiredArgsConstructor
public class BannerPublicController {

    private final BannerService bannerService;

    @Operation(summary = "Get all active banners (cached)")
    @GetMapping
    public ResponseEntity<ApiResponse<List<BannerResponse>>> getActiveBanners() {
        return ResponseEntity.ok(ApiResponse.success(bannerService.getActiveBanners()));
    }
}