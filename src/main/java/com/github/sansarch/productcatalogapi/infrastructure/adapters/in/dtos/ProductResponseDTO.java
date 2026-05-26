package com.github.sansarch.productcatalogapi.infrastructure.adapters.in.dtos;

import java.math.BigDecimal;

public record ProductResponseDTO(
        String id,
        String name,
        String category,
        String sku,
        BigDecimal price,
        String description
) {
}
