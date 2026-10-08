package com.lotosia.catalog.service.impl;

import com.lotosia.catalog.dto.request.BannerRequest;
import com.lotosia.catalog.dto.response.BannerResponse;
import com.lotosia.catalog.entity.Banner;
import com.lotosia.catalog.exception.BannerNotFoundException;
import com.lotosia.catalog.repository.BannerRepository;
import com.lotosia.catalog.service.BannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: nijataghayev
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BannerServiceImpl implements BannerService {

    private static final String CACHE_ACTIVE = "banners:active";

    private final BannerRepository bannerRepository;

    @Override
    @Cacheable(value = CACHE_ACTIVE)
    public List<BannerResponse> getActiveBanners() {
        log.debug("Fetching active banners from DB");
        return bannerRepository.findByActiveTrueOrderByCreatedDateDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<BannerResponse> getAllBanners() {
        return bannerRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public BannerResponse getBannerById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_ACTIVE, allEntries = true)
    public BannerResponse createBanner(BannerRequest request) {
        Banner banner = Banner.builder()
                .title(request.title())
                .description(request.description())
                .imageUrl(request.imageUrl())
                .active(request.active())
                .build();

        Banner saved = bannerRepository.save(banner);
        log.info("Banner created: id={}, active={}", saved.getId(), saved.isActive());
        return toResponse(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_ACTIVE, allEntries = true)
    public BannerResponse updateBanner(Long id, BannerRequest request) {
        Banner banner = findOrThrow(id);

        banner.setTitle(request.title());
        banner.setDescription(request.description());
        banner.setImageUrl(request.imageUrl());
        banner.setActive(request.active());

        log.info("Banner updated: id={}", id);
        return toResponse(banner);
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_ACTIVE, allEntries = true)
    public BannerResponse setActive(Long id, boolean active) {
        Banner banner = findOrThrow(id);
        banner.setActive(active);
        log.info("Banner id={} active set to {}", id, active);
        return toResponse(banner);
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_ACTIVE, allEntries = true)
    public void deleteBanner(Long id) {
        if (!bannerRepository.existsById(id)) {
            throw new BannerNotFoundException(id);
        }
        bannerRepository.deleteById(id);
        log.info("Banner deleted: id={}", id);
    }

    private Banner findOrThrow(Long id) {
        return bannerRepository.findById(id)
                .orElseThrow(() -> new BannerNotFoundException(id));
    }

    private BannerResponse toResponse(Banner b) {
        return BannerResponse.builder()
                .id(b.getId())
                .title(b.getTitle())
                .description(b.getDescription())
                .imageUrl(b.getImageUrl())
                .active(b.isActive())
                .createdDate(b.getCreatedDate())
                .build();
    }
}