package com.lotosia.catalog.service.impl;

import com.lotosia.catalog.dto.MainImage;
import com.lotosia.catalog.dto.request.ProductFilterRequest;
import com.lotosia.catalog.dto.request.ProductRequest;
import com.lotosia.catalog.dto.request.ProductUpdateRequest;
import com.lotosia.catalog.dto.response.CategoryResponse;
import com.lotosia.catalog.dto.response.ProductDetailResponse;
import com.lotosia.catalog.dto.response.ProductResponse;
import com.lotosia.catalog.entity.Category;
import com.lotosia.catalog.entity.Product;
import com.lotosia.catalog.entity.ProductImage;
import com.lotosia.catalog.exception.CategoryNotFoundException;
import com.lotosia.catalog.exception.ConflictException;
import com.lotosia.catalog.exception.ResourceNotFoundException;
import com.lotosia.catalog.repository.CategoryRepository;
import com.lotosia.catalog.repository.ProductImageRepository;
import com.lotosia.catalog.repository.ProductRepository;
import com.lotosia.catalog.service.ProductService;
import com.lotosia.catalog.specification.ProductSpecification;
import com.lotosia.catalog.enums.ProductSortType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: nijataghayev
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Page<ProductResponse> filterProducts(ProductFilterRequest filter) {
        Specification<Product> spec = ProductSpecification.from(filter);
        Pageable pageable = PageRequest.of(filter.page(), filter.size(), resolveSort(filter.sortBy()));

        Page<Product> page = productRepository.findAll(spec, pageable);

        Set<Long> ids = page.stream().map(Product::getId).collect(Collectors.toSet());
        Map<Long, String> mainImages = ids.isEmpty()
                ? Map.of()
                : productImageRepository.findMainImages(ids)
                        .stream()
                        .collect(Collectors.toMap(MainImage::productId, MainImage::url, (a, b) -> a));

        return page.map(p -> toProductResponse(p, mainImages.get(p.getId())));
    }

    @Override
    public ProductDetailResponse getProductById(Long id) {
        Product product = productRepository.findWithDetailsByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return toDetailResponse(product);
    }

    @Override
    public List<ProductResponse> getLatestProducts(int limit) {
        List<Product> products = productRepository
                .findByActiveTrueOrderByCreatedAtDescIdDesc(PageRequest.of(0, limit));

        Set<Long> ids = products.stream().map(Product::getId).collect(Collectors.toSet());
        Map<Long, String> mainImages = ids.isEmpty()
                ? Map.of()
                : productImageRepository.findMainImages(ids)
                        .stream()
                        .collect(Collectors.toMap(MainImage::productId, MainImage::url, (a, b) -> a));

        return products.stream()
                .map(p -> toProductResponse(p, mainImages.get(p.getId())))
                .toList();
    }

    @Override
    public List<ProductResponse> getForYouProducts(int limit) {
        List<Long> randomIds = productRepository.findRandomIds(limit);
        if (randomIds.isEmpty()) return List.of();

        List<Product> products = productRepository.findByIdIn(randomIds);
        Map<Long, String> mainImages = productImageRepository.findMainImages(randomIds)
                .stream()
                .collect(Collectors.toMap(MainImage::productId, MainImage::url, (a, b) -> a));

        return products.stream()
                .map(p -> toProductResponse(p, mainImages.get(p.getId())))
                .toList();
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsBySku(request.sku())) {
            throw new ConflictException("Product with SKU '" + request.sku() + "' already exists");
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(request.categoryId()));

        Product product = new Product();
        product.setSku(request.sku());
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setCategory(category);
        product.setActive(true);

        Product saved = productRepository.save(product);
        log.info("Product created: id={}, sku={}", saved.getId(), saved.getSku());
        return toProductResponse(saved, null);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());

        log.info("Product updated: id={}", id);
        return toProductResponse(product, null);
    }

    @Override
    @Transactional
    public ProductResponse setActive(Long id, boolean active) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        product.setActive(active);
        log.info("Product id={} active set to {}", id, active);
        return toProductResponse(product, null);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
        log.info("Product deleted: id={}", id);
    }

    private Sort resolveSort(ProductSortType sortType) {
        return switch (sortType) {
            case PRICE_ASC  -> Sort.by("price").ascending();
            case PRICE_DESC -> Sort.by("price").descending();
            case NAME_ASC   -> Sort.by("name").ascending();
            case NEWEST     -> Sort.by("createdDate").descending().and(Sort.by("id").descending());
        };
    }

    private ProductResponse toProductResponse(Product p, String mainImageUrl) {
        String[] images = mainImageUrl != null ? new String[]{mainImageUrl} : new String[0];
        return ProductResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .sku(p.getSku())
                .price(p.getPrice() != null ? p.getPrice().doubleValue() : 0.0)
                .images(images)
                .categoryId(p.getCategory() != null ? p.getCategory().getId() : null)
                .build();
    }

    private ProductDetailResponse toDetailResponse(Product p) {
        List<String> images = p.getImages().stream()
                .map(ProductImage::getUrl)
                .toList();

        CategoryResponse categoryResponse = null;
        if (p.getCategory() != null) {
            Category c = p.getCategory();
            categoryResponse = CategoryResponse.builder()
                    .id(c.getId())
                    .name(c.getName())
                    .description(c.getDescription())
                    .imageUrl(c.getImageUrl())
                    .slug(c.getSlug())
                    .productCount(0)
                    .build();
        }

        return ProductDetailResponse.builder()
                .id(p.getId())
                .sku(p.getSku())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .active(p.isActive())
                .images(images)
                .category(categoryResponse)
                .build();
    }
}