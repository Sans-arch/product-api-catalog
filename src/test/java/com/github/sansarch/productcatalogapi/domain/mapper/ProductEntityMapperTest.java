package com.github.sansarch.productcatalogapi.domain.mapper;

import com.github.sansarch.productcatalogapi.core.model.product.Product;
import com.github.sansarch.productcatalogapi.core.model.product.vo.ProductId;
import com.github.sansarch.productcatalogapi.infrastructure.adapters.in.dtos.ProductRequestDTO;
import com.github.sansarch.productcatalogapi.infrastructure.adapters.in.dtos.ProductResponseDTO;
import com.github.sansarch.productcatalogapi.infrastructure.adapters.in.mappers.ProductMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductEntityMapperTest {

    private final ProductMapper mapper = new ProductMapper();

    @Test
    @DisplayName("should map all fields from ProductRequestDTO to domain Product")
    void shouldMapAllFieldsFromRequestToDomain() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product", "Test Category", "TESTSKU", new BigDecimal("99.99"), "Test Description"
        );

        Product product = mapper.toDomain(dto);

        assertEquals(dto.name(), product.getName());
        assertEquals(dto.category(), product.getCategory());
        assertEquals(dto.sku(), product.getSku());
        assertEquals(dto.price(), product.getPrice());
        assertEquals(dto.description(), product.getDescription());
    }

    @Test
    @DisplayName("should map all fields from domain Product to ProductResponseDTO")
    void shouldMapAllFieldsFromDomainToResponse() {
        ProductId id = ProductId.generate();
        Product product = new Product(
            id, "Test Product", "Test Category", "TESTSKU", new BigDecimal("99.99"), "Test Description"
        );

        ProductResponseDTO response = mapper.toResponse(product);

        assertEquals(id.value().toString(), response.id());
        assertEquals(product.getName(), response.name());
        assertEquals(product.getCategory(), response.category());
        assertEquals(product.getSku(), response.sku());
        assertEquals(product.getPrice(), response.price());
        assertEquals(product.getDescription(), response.description());
    }

    @Test
    @DisplayName("should generate a new UUID id when mapping from ProductRequestDTO to domain Product")
    void shouldGenerateIdWhenMappingFromRequestToDomain() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product", "Test Category", "TESTSKU", new BigDecimal("99.99"), "Test Description"
        );

        Product product = mapper.toDomain(dto);

        assertThat(product.getId()).isNotNull();
        assertThat(product.getId().value()).isNotNull();
    }
}
