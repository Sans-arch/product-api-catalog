package com.github.sansarch.productcatalogapi.infrastructure.adapters.in.controllers;

import com.github.sansarch.productcatalogapi.core.model.product.vo.ProductId;
import com.github.sansarch.productcatalogapi.core.shared.pagination.PageRequest;
import com.github.sansarch.productcatalogapi.core.usecase.product.create.CreateProductUseCase;
import com.github.sansarch.productcatalogapi.core.usecase.product.delete.DeleteProductUseCase;
import com.github.sansarch.productcatalogapi.core.usecase.product.find.FindProductUseCase;
import com.github.sansarch.productcatalogapi.core.usecase.product.list.ListProductsUseCase;
import com.github.sansarch.productcatalogapi.core.usecase.product.listbycategory.ListProductsByCategoryUseCase;
import com.github.sansarch.productcatalogapi.core.usecase.product.listbycategoryunderprice.ListByCategoryUnderPriceUseCase;
import com.github.sansarch.productcatalogapi.core.usecase.product.update.UpdateProductUseCase;
import com.github.sansarch.productcatalogapi.infrastructure.adapters.in.dtos.PageResponseDTO;
import com.github.sansarch.productcatalogapi.infrastructure.adapters.in.dtos.ProductRequestDTO;
import com.github.sansarch.productcatalogapi.infrastructure.adapters.in.dtos.ProductResponseDTO;
import com.github.sansarch.productcatalogapi.infrastructure.adapters.in.mappers.ProductMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final CreateProductUseCase createProduct;
    private final FindProductUseCase findProduct;
    private final ListProductsUseCase listProducts;
    private final ListProductsByCategoryUseCase listByCategory;
    private final ListByCategoryUnderPriceUseCase listByCategoryUnderPrice;
    private final UpdateProductUseCase updateProduct;
    private final DeleteProductUseCase deleteProduct;
    private final ProductMapper mapper;

    public ProductController(
        CreateProductUseCase createProduct,
        FindProductUseCase findProduct,
        ListProductsUseCase listProducts,
        ListProductsByCategoryUseCase listByCategory,
        ListByCategoryUnderPriceUseCase listByCategoryUnderPrice,
        UpdateProductUseCase updateProduct,
        DeleteProductUseCase deleteProduct,
        ProductMapper mapper
    ) {
        this.createProduct = createProduct;
        this.findProduct = findProduct;
        this.listProducts = listProducts;
        this.listByCategory = listByCategory;
        this.listByCategoryUnderPrice = listByCategoryUnderPrice;
        this.updateProduct = updateProduct;
        this.deleteProduct = deleteProduct;
        this.mapper = mapper;
    }

    @GetMapping
    public PageResponseDTO<ProductResponseDTO> getAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String sort
    ) {
        return PageResponseDTO.from(listProducts.execute(PageRequest.of(page, size, sort)), mapper::toResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(mapper.toResponse(findProduct.byId(ProductId.from(id))));
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductResponseDTO> getBySku(@PathVariable String sku) {
        return ResponseEntity.ok(mapper.toResponse(findProduct.bySku(sku)));
    }

    @GetMapping("/category/{category}")
    public PageResponseDTO<ProductResponseDTO> getByCategory(
        @PathVariable String category,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String sort
    ) {
        return PageResponseDTO.from(listByCategory.execute(category, PageRequest.of(page, size, sort)), mapper::toResponse);
    }

    @GetMapping("/category/{category}/under/{maxPrice}")
    public PageResponseDTO<ProductResponseDTO> getByCategoryUnderPrice(
        @PathVariable String category,
        @PathVariable BigDecimal maxPrice,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String sort
    ) {
        return PageResponseDTO.from(
            listByCategoryUnderPrice.execute(category, maxPrice, PageRequest.of(page, size, sort)),
            mapper::toResponse
        );
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@Valid @RequestBody ProductRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(mapper.toResponse(createProduct.execute(mapper.toDomain(dto))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable String id, @Valid @RequestBody ProductRequestDTO dto) {
        return ResponseEntity.ok(
            mapper.toResponse(updateProduct.execute(ProductId.from(id), mapper.toDomain(dto)))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        deleteProduct.execute(ProductId.from(id));
        return ResponseEntity.noContent().build();
    }
}
