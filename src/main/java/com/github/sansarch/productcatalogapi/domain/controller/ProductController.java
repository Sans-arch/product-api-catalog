package com.github.sansarch.productcatalogapi.domain.controller;

import com.github.sansarch.productcatalogapi.domain.dto.ProductRequestDTO;
import com.github.sansarch.productcatalogapi.domain.dto.ProductResponseDTO;
import com.github.sansarch.productcatalogapi.domain.entity.Product;
import com.github.sansarch.productcatalogapi.domain.mapper.ProductMapper;
import com.github.sansarch.productcatalogapi.domain.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;
    private final ProductMapper mapper;

    public ProductController(ProductService service, ProductMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<ProductResponseDTO> getAll() {
        return service.getAllProducts().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.getById(id)));
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductResponseDTO> getBySku(@PathVariable String sku) {
        return ResponseEntity.ok(mapper.toResponse(service.getBySku(sku)));
    }

    @GetMapping("/category/{category}")
    public List<ProductResponseDTO> getByCategory(@PathVariable String category) {
        return service.getByCategory(category).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @GetMapping("/category/{category}/under/{maxPrice}")
    public List<ProductResponseDTO> getByCategoryUnderPrice(@PathVariable String category, @PathVariable Double maxPrice) {
        return service.getByCategoryUnderPrice(category, maxPrice).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@Valid @RequestBody ProductRequestDTO dto) {
        Product saved = service.save(mapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO dto) {
        Product updated = service.update(id, mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
