package com.github.sansarch.productcatalogapi.domain.service;

import com.github.sansarch.productcatalogapi.domain.entity.Product;
import com.github.sansarch.productcatalogapi.domain.exception.DuplicateSkuException;
import com.github.sansarch.productcatalogapi.domain.exception.ProductNotFoundException;
import com.github.sansarch.productcatalogapi.domain.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final ProductQueryService queryService;

    public ProductService(ProductRepository repository, ProductQueryService queryService) {
        this.repository = repository;
        this.queryService = queryService;
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Product getById(Long id) {
        return queryService.getById(id);
    }

    public Product getBySku(String sku) {
        return queryService.getBySku(sku);
    }

    public List<Product> getByCategory(String category) {
        return queryService.getByCategory(category);
    }

    public List<Product> getByCategoryUnderPrice(String category, Double maxPrice) {
        return queryService.getByCategoryUnderPrice(category, maxPrice);
    }

    // @CachePut updates the cache when you save -- keeps it in sync
    @CachePut(value = "products", key = "#result.id")
    public Product save(Product product) {
        if (repository.findBySku(product.getSku()).isPresent()) {
            throw new DuplicateSkuException(product.getSku());
        }
        return repository.save(product);
    }

    // @CacheEvict removes the entry when you delete
    @CacheEvict(value = "products", key = "#id", beforeInvocation = true)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        repository.deleteById(id);
    }

    @CachePut(value = "products", key = "#result.id")
    @CacheEvict(value = "products-by-category", allEntries = true)
    // evict all category caches since we don't know which one changed
    public Product update(Long id, Product updated) {
        Product existing = queryService.getById(id);

        if (!existing.getSku().equals(updated.getSku()) && repository.findBySku(updated.getSku()).isPresent()) {
            throw new DuplicateSkuException(updated.getSku());
        }

        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        existing.setCategory(updated.getCategory());
        existing.setSku(updated.getSku());

        return repository.save(existing);
    }
}
