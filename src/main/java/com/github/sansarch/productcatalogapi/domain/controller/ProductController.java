package com.github.sansarch.productcatalogapi.domain.controller;

import com.github.sansarch.productcatalogapi.domain.dto.ProductRequestDTO;
import com.github.sansarch.productcatalogapi.domain.dto.ProductResponseDTO;
import com.github.sansarch.productcatalogapi.domain.entity.Product;
import com.github.sansarch.productcatalogapi.domain.mapper.ProductMapper;
import com.github.sansarch.productcatalogapi.domain.service.ProductService;
import com.github.sansarch.productcatalogapi.domain.shared.PageResponseDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

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
    public PageResponseDTO<ProductResponseDTO> getAll(@PageableDefault(size = 10, sort = "name") Pageable pageable) {
       return PageResponseDTO.from(service.getAllProducts(pageable).map(mapper::toResponse));
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
    public PageResponseDTO<ProductResponseDTO> getByCategory(@PathVariable String category, @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        return PageResponseDTO.from(
                service.getByCategory(category, pageable).map(mapper::toResponse)
        );
    }

    @GetMapping("/category/{category}/under/{maxPrice}")
    public PageResponseDTO<ProductResponseDTO> getByCategoryUnderPrice(@PathVariable String category,
                                                                       @PathVariable BigDecimal maxPrice,
                                                                       @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        return PageResponseDTO.from(
                service.getByCategoryUnderPrice(category, maxPrice, pageable).map(mapper::toResponse)
        );
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
