package com.lotosia.catalog.service;

import com.lotosia.catalog.dto.request.BannerRequest;
import com.lotosia.catalog.dto.response.BannerResponse;

import java.util.List;

/**
 * @author: nijataghayev
 */

public interface BannerService {

    List<BannerResponse> getActiveBanners();

    List<BannerResponse> getAllBanners();

    BannerResponse getBannerById(Long id);

    BannerResponse createBanner(BannerRequest request);

    BannerResponse updateBanner(Long id, BannerRequest request);

    BannerResponse setActive(Long id, boolean active);

    void deleteBanner(Long id);
}