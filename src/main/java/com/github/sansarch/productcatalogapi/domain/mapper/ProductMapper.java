package com.github.sansarch.productcatalogapi.domain.mapper;

import com.github.sansarch.productcatalogapi.domain.dto.ProductRequestDTO;
import com.github.sansarch.productcatalogapi.domain.dto.ProductResponseDTO;
import com.github.sansarch.productcatalogapi.domain.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequestDTO dto) {
        Product product = new Product();
        product.setName(dto.name());
        product.setCategory(dto.category());
        product.setSku(dto.sku());
        product.setPrice(dto.price());
        product.setDescription(dto.description());
        return product;
    }

    public ProductResponseDTO toResponse(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getSku(),
                product.getPrice(),
                product.getDescription()
        );
    }
}
