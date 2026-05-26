package com.github.sansarch.productcatalogapi.core.model.product.vo;

import java.util.UUID;

public record ProductId(UUID value) {
    public ProductId {
        if (value == null) {
            throw new IllegalArgumentException("ProductId cannot be null");
        }
    }

    public static ProductId generate() {
        return new ProductId(UUID.randomUUID());
    }

    public static ProductId from(String uuid) {
        return new ProductId(UUID.fromString(uuid));
    }
}
