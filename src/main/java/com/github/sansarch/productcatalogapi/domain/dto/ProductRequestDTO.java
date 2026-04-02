package com.github.sansarch.productcatalogapi.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequestDTO(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Category is required")
        String category,

        @NotBlank(message = "SKU is required")
        String sku,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        BigDecimal price,

        String description
) {
}
