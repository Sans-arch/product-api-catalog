package com.github.sansarch.productcatalogapi.domain.service;

import com.github.sansarch.productcatalogapi.domain.entity.Product;
import com.github.sansarch.productcatalogapi.domain.exception.ProductNotFoundException;
import com.github.sansarch.productcatalogapi.domain.repository.ProductRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductQueryService {

    private final ProductRepository repository;

    public ProductQueryService(ProductRepository repository) {
        this.repository = repository;
    }

    // Pagination + sorting — not cached because the key space is too large
    public Page<Product> getAllProducts(Pageable pageable) {
        return repository.findAll(pageable);
    }

    // Cache a single product by its ID
    // Key: "products::1", "products::2", etc.
    @Cacheable(value = "products", key = "#id")
    public Product getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    // Cache by SKU
    @Cacheable(value = "products", key = "#sku")
    public Product getBySku(String sku) {
        return repository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException(sku));
    }

    @Cacheable(value = "products-by-category", key = "#category + '-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort")
    public Page<Product> getByCategory(String category, Pageable pageable) {
        return repository.findByCategory(category, pageable);
    }

    @Cacheable(value = "products-by-category", key = "#category + '-' + #maxPrice + '-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort")
    public Page<Product> getByCategoryUnderPrice(String category, BigDecimal maxPrice, Pageable pageable) {
        return repository.findByCategoryAndPriceLessThan(category, maxPrice, pageable);
    }
}
