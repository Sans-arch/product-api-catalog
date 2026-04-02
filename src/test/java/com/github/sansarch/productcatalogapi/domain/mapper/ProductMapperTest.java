package com.github.sansarch.productcatalogapi.domain.mapper;

import com.github.sansarch.productcatalogapi.domain.dto.ProductRequestDTO;
import com.github.sansarch.productcatalogapi.domain.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductMapperTest {

    private final ProductMapper mapper = new ProductMapper();

    @Test
    @DisplayName("should map all fields from ProductRequestDTO to Product entity")
    void shouldMapAllFieldsFromRequestToEntity() {
        ProductRequestDTO dto = new ProductRequestDTO("Test Product", "Test Category", "TESTSKU", new BigDecimal("99.99"), "Test Description");
        Product entity = mapper.toEntity(dto);

        assertEquals(dto.name(), entity.getName());
        assertEquals(dto.category(), entity.getCategory());
        assertEquals(dto.sku(), entity.getSku());
        assertEquals(dto.price(), entity.getPrice());
        assertEquals(dto.description(), entity.getDescription());
    }

    @Test
    @DisplayName("should map all fields from Product entity to ProductResponseDTO")
    void shouldMapAllFieldsFromEntityToResponse() {
        Product entity = new Product();
        entity.setId(1L);
        entity.setName("Test Product");
        entity.setCategory("Test Category");
        entity.setSku("TESTSKU");
        entity.setPrice(new BigDecimal("99.99"));
        entity.setDescription("Test Description");

        var response = mapper.toResponse(entity);

        assertEquals(entity.getId(), response.id());
        assertEquals(entity.getName(), response.name());
        assertEquals(entity.getCategory(), response.category());
        assertEquals(entity.getSku(), response.sku());
        assertEquals(entity.getPrice(), response.price());
        assertEquals(entity.getDescription(), response.description());
    }

    @Test
    @DisplayName("should never set id when mapping from ProductRequestDTO to Product entity")
    void shouldNeverSetIdWhenMappingFromRequestToEntity() {
        ProductRequestDTO dto = new ProductRequestDTO("Test Product", "Test Category", "TESTSKU", new BigDecimal("99.99"), "Test Description");
        Product entity = mapper.toEntity(dto);

        assertThat(entity.getId()).isNull();
    }
}