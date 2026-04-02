package com.github.sansarch.productcatalogapi.domain.service;

import com.github.sansarch.productcatalogapi.domain.entity.Product;
import com.github.sansarch.productcatalogapi.domain.exception.DuplicateSkuException;
import com.github.sansarch.productcatalogapi.domain.exception.ProductNotFoundException;
import com.github.sansarch.productcatalogapi.domain.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;
    private ProductService self;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setSelf(ProductService self) {
        this.self = self; // for calling methods with caching from within the same class
    }

    // No cache here -- listing everything changes too often
    public List<Product> getAllProducts() {
        return repository.findAll();
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

    // @CachePut updates the cache when you save -- keeps it in sync
    @CachePut(value = "products", key = "#result.id")
    public Product save(Product product) {
        if (repository.findBySku(product.getSku()).isPresent()) {
            throw new DuplicateSkuException(product.getSku());
        }
        return repository.save(product);
    }

    // @CacheEvict removes the entry when you delete
    @CacheEvict(value = "products", key = "#i")
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @CachePut(value = "products", key = "#result.id")
    @CacheEvict(value = "products-by-category", allEntries = true)
    // evict all category caches since we don't know which one changed
    public Product update(Long id, Product updated) {
        Product existing = self.getById(id);

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
