package com.lotosia.catalog.service.impl;

import com.lotosia.catalog.dto.request.CategoryRequest;
import com.lotosia.catalog.dto.response.CategoryResponse;
import com.lotosia.catalog.entity.Category;
import com.lotosia.catalog.exception.CategoryNotFoundException;
import com.lotosia.catalog.exception.ConflictException;
import com.lotosia.catalog.repository.CategoryRepository;
import com.lotosia.catalog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

/**
 * @author: nijataghayev
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private static final String CACHE_KEY = "categories";

    private final CategoryRepository categoryRepository;

    @Override
    @Cacheable(value = CACHE_KEY)
    public List<CategoryResponse> getAllCategories() {
        log.debug("Fetching all categories from DB");
        return categoryRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        return toResponse(category);
    }

    @Override
    public CategoryResponse getCategoryBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new CategoryNotFoundException(slug));
        return toResponse(category);
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_KEY, allEntries = true)
    public CategoryResponse createCategory(CategoryRequest request) {
        guardDuplicateName(request.name(), null);

        String slug = toSlug(request.name());
        guardDuplicateSlug(slug, null);

        Category category = new Category();
        category.setName(request.name().strip());
        category.setDescription(request.description());
        category.setImageUrl(request.imageUrl());
        category.setSlug(slug);

        Category saved = categoryRepository.save(category);
        log.info("Category created: id={}, slug={}", saved.getId(), saved.getSlug());
        return toResponse(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_KEY, allEntries = true)
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        guardDuplicateName(request.name(), id);

        String newSlug = toSlug(request.name());
        if (!newSlug.equals(category.getSlug())) {
            guardDuplicateSlug(newSlug, id);
            category.setSlug(newSlug);
        }

        category.setName(request.name().strip());
        category.setDescription(request.description());
        category.setImageUrl(request.imageUrl());

        log.info("Category updated: id={}, slug={}", id, category.getSlug());
        return toResponse(category);
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_KEY, allEntries = true)
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException(id);
        }
        categoryRepository.deleteById(id);
        log.info("Category deleted: id={}", id);
    }

    private String toSlug(String input) {
        if (input == null || input.isBlank()) return "";

        String mapped = input
                .replace("\u0259", "e").replace("\u018F", "e")
                .replace("\u0131", "i").replace("\u0130", "i")
                .replace("\u011F", "g").replace("\u011E", "g")
                .replace("\u015F", "s").replace("\u015E", "s")
                .replace("\u00E7", "c").replace("\u00C7", "c")
                .replace("\u00F6", "o").replace("\u00D6", "o")
                .replace("\u00FC", "u").replace("\u00DC", "u");

        String normalized = Normalizer.normalize(mapped, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        return normalized.toLowerCase(Locale.ENGLISH)
                .replaceAll("[^a-z0-9\\s-]", "")
                .strip()
                .replaceAll("\\s+", "-")
                .replaceAll("-{2,}", "-");
    }

    private void guardDuplicateName(String name, Long excludeId) {
        categoryRepository.findAll().stream()
                .filter(c -> c.getName().equalsIgnoreCase(name.strip()))
                .filter(c -> !c.getId().equals(excludeId))
                .findFirst()
                .ifPresent(c -> {
                    throw new ConflictException("Category with name '" + name + "' already exists");
                });
    }

    private void guardDuplicateSlug(String slug, Long excludeId) {
        categoryRepository.findBySlug(slug).ifPresent(c -> {
            if (!c.getId().equals(excludeId)) {
                throw new ConflictException("Category with slug '" + slug + "' already exists");
            }
        });
    }

    private CategoryResponse toResponse(Category c) {
        int count = c.getProducts() != null ? c.getProducts().size() : 0;
        return CategoryResponse.builder()
                .id(c.getId())
                .name(c.getName())
                .description(c.getDescription())
                .imageUrl(c.getImageUrl())
                .slug(c.getSlug())
                .productCount(count)
                .build();
    }
}