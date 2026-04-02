package com.github.sansarch.productcatalogapi.domain.dto;

import java.math.BigDecimal;

public record ProductResponseDTO(
        Long id,
        String name,
        String category,
        String sku,
        BigDecimal price,
        String description
) {
}
