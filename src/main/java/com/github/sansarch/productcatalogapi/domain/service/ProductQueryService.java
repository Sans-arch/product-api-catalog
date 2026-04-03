package com.github.sansarch.productcatalogapi.domain.service;

import com.github.sansarch.productcatalogapi.domain.entity.Product;
import com.github.sansarch.productcatalogapi.domain.exception.ProductNotFoundException;
import com.github.sansarch.productcatalogapi.domain.repository.ProductRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductQueryService {

    private final ProductRepository repository;

    public ProductQueryService(ProductRepository repository) {
        this.repository = repository;
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

    // Cache the entire list for a given category
    @Cacheable(value = "products-by-category", key = "#category")
    public List<Product> getByCategory(String category) {
        return repository.findByCategory(category);
    }

    // Cache by category + price combination
    @Cacheable(value = "products-by-category", key = "#category + '-' + #maxPrice")
    public List<Product> getByCategoryUnderPrice(String category, Double maxPrice) {
        return repository.findByCategoryAndPriceLessThan(category, maxPrice);
    }
}
