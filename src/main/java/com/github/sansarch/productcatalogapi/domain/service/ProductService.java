package com.github.sansarch.productcatalogapi.domain.service;

import com.github.sansarch.productcatalogapi.domain.entity.Product;
import com.github.sansarch.productcatalogapi.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Product getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    public Product getBySku(String sku) {
        return repository.findBySku(sku)
                .orElseThrow(() -> new RuntimeException("Product not found: " + sku));
    }

    public List<Product> getByCategory(String category) {
        return repository.findByCategory(category);
    }

    public List<Product> getByCategoryUnderPrice(String category, Double maxPrice) {
        return repository.findByCategoryAndPriceLessThan(category, maxPrice);
    }

    public Product save(Product product) {
        return repository.save(product);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
