package com.github.sansarch.productcatalogapi.infrastructure.adapters.in.mappers;

import com.github.sansarch.productcatalogapi.core.model.product.Product;
import com.github.sansarch.productcatalogapi.infrastructure.adapters.in.dtos.ProductRequestDTO;
import com.github.sansarch.productcatalogapi.infrastructure.adapters.in.dtos.ProductResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toDomain(ProductRequestDTO dto) {
        return new Product(
            dto.name(),
            dto.category(),
            dto.sku(),
            dto.price(),
            dto.description()
        );
    }

    public ProductResponseDTO toResponse(Product product) {
        return new ProductResponseDTO(
                product.getId().value().toString(),
                product.getName(),
                product.getCategory(),
                product.getSku(),
                product.getPrice(),
                product.getDescription()
        );
    }
}
