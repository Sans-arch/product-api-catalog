package com.github.sansarch.productcatalogapi.domain.controller;

import com.github.sansarch.productcatalogapi.domain.entity.Product;
import com.github.sansarch.productcatalogapi.domain.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<Product> getAll() {
        return service.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<Product> getBySku(@PathVariable String sku) {
        return ResponseEntity.ok(service.getBySku(sku));
    }

    @GetMapping("/category/{category}")
    public List<Product> getByCategory(@PathVariable String category) {
        return service.getByCategory(category);
    }

    @GetMapping("/category/{category}/under/{maxPrice}")
    public List<Product> getByCategoryUnderPrice(@PathVariable String category, @PathVariable Double maxPrice) {
        return service.getByCategoryUnderPrice(category, maxPrice);
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        return ResponseEntity.ok(service.save(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
