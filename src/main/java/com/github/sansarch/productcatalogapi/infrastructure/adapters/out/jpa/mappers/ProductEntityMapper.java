package com.github.sansarch.productcatalogapi.infrastructure.adapters.out.jpa.mappers;

import com.github.sansarch.productcatalogapi.core.model.product.Product;
import com.github.sansarch.productcatalogapi.core.model.product.vo.ProductId;
import com.github.sansarch.productcatalogapi.infrastructure.adapters.out.jpa.entities.ProductEntity;

public class ProductEntityMapper {

    private ProductEntityMapper() {}

    public static Product toDomain(ProductEntity entity) {
        return new Product(
            new ProductId(entity.getId()),
            entity.getName(),
            entity.getCategory(),
            entity.getSku(),
            entity.getPrice(),
            entity.getDescription()
        );
    }

    public static ProductEntity toEntity(Product domain) {
        ProductEntity entity = new ProductEntity();
        entity.setId(domain.getId().value());
        entity.setName(domain.getName());
        entity.setCategory(domain.getCategory());
        entity.setSku(domain.getSku());
        entity.setPrice(domain.getPrice());
        entity.setDescription(domain.getDescription());
        return entity;
    }
}
