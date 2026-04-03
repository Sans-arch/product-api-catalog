package com.github.sansarch.productcatalogapi.domain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sansarch.productcatalogapi.BaseIntegrationTest;
import com.github.sansarch.productcatalogapi.domain.dto.ProductRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final ProductRequestDTO MACBOOK = new ProductRequestDTO(
            "MacBook Pro",
            "Electronics",
            "MBP-001",
            new BigDecimal("1999.99"),
            "Apple M3"
    );

    private static final ProductRequestDTO CHAIR = new ProductRequestDTO(
            "Office Chair",
            "Furniture",
            "OFC-123",
            new BigDecimal("149.99"),
            "Ergonomic office chair with lumbar support"
    );

    private long createProduct(ProductRequestDTO dto) throws Exception {
        String response = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }

    @Test
    @DisplayName("should create a new product and return 201 Created")
    void shouldCreateProductAndReturn201() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(MACBOOK)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("MacBook Pro"))
                .andExpect(jsonPath("$.category").value("Electronics"))
                .andExpect(jsonPath("$.sku").value("MBP-001"))
                .andExpect(jsonPath("$.price").value(1999.99))
                .andExpect(jsonPath("$.description").value("Apple M3"));
    }

    @Test
    @DisplayName("should return 404 when product is not found")
    void shouldReturn404WhenProductNotFound() throws Exception {
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(containsString("999")));
    }

    @Test
    @DisplayName("should return 409 when SKU already exists")
    void shouldReturn409WhenSkuAlreadyExists() throws Exception {
        createProduct(MACBOOK);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(MACBOOK)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value(containsString("MBP-001")));
    }

    @Test
    @DisplayName("should return 400 when request body is invalid")
    void shouldReturn400WhenRequestIsInvalid() throws Exception {
        ProductRequestDTO invalid = new ProductRequestDTO(
                "", "Electronics", "MBP-001", new BigDecimal("-50"), "Apple M3"
        );

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages").isArray());
    }

    @Test
    @DisplayName("should return all products")
    void shouldReturnAllProducts() throws Exception {
        createProduct(MACBOOK);
        createProduct(CHAIR);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    @DisplayName("should return product by id")
    void shouldReturnProductById() throws Exception {
        long id = createProduct(MACBOOK);

        mockMvc.perform(get("/api/products/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("MacBook Pro"));
    }

    @Test
    @DisplayName("should return product by SKU")
    void shouldReturnProductBySku() throws Exception {
        createProduct(MACBOOK);

        mockMvc.perform(get("/api/products/sku/MBP-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("MBP-001"));
    }

    @Test
    @DisplayName("should return products filtered by category")
    void shouldReturnProductsByCategory() throws Exception {
        createProduct(MACBOOK);
        createProduct(CHAIR);

        mockMvc.perform(get("/api/products/category/Electronics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].category").value("Electronics"));
    }

    @Test
    @DisplayName("should return products filtered by category under a max price")
    void shouldReturnProductsByCategoryUnderPrice() throws Exception {
        createProduct(MACBOOK); // 1999.99

        ProductRequestDTO iphone = new ProductRequestDTO(
                "iPhone 15", "Electronics", "IPH-015", new BigDecimal("999.99"), "A17 chip"
        );
        createProduct(iphone);
        createProduct(CHAIR); // Furniture — different category, should not appear

        mockMvc.perform(get("/api/products/category/Electronics/under/1500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name").value("iPhone 15"));
    }

    @Test
    @DisplayName("should update product and return 200")
    void shouldUpdateProductAndReturn200() throws Exception {
        long id = createProduct(MACBOOK);

        ProductRequestDTO updated = new ProductRequestDTO(
                "MacBook Pro M3", "Electronics", "MBP-001", new BigDecimal("1799.99"), "Updated"
        );

        mockMvc.perform(put("/api/products/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("MacBook Pro M3"))
                .andExpect(jsonPath("$.price").value(1799.99));
    }

    @Test
    @DisplayName("should return 404 when updating a non-existent product")
    void shouldReturn404WhenUpdatingNonExistentProduct() throws Exception {
        mockMvc.perform(put("/api/products/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(MACBOOK)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("should delete product and return 204")
    void shouldDeleteProductAndReturn204() throws Exception {
        long id = createProduct(MACBOOK);

        mockMvc.perform(delete("/api/products/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/products/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should serve cached product without hitting the database")
    void shouldServeCachedProductWithoutHittingDatabase() throws Exception {
        long id = createProduct(MACBOOK);

        // First request — populates cache
        mockMvc.perform(get("/api/products/" + id))
                .andExpect(status().isOk());

        // Confirm key exists in cache manager
        var productsCache = cacheManager.getCache("products");
        assertThat(productsCache).isNotNull();
        assertThat(productsCache.get(id)).isNotNull();

        // Delete directly from DB, bypassing the service
        productRepository.deleteAll();

        // Second request — should still be served from cache
        mockMvc.perform(get("/api/products/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("MacBook Pro"));
    }

    @Test
    @DisplayName("should evict cache after product deletion")
    void shouldEvictCacheAfterDeletion() throws Exception {
        long id = createProduct(MACBOOK);

        // Populate cache
        mockMvc.perform(get("/api/products/" + id))
                .andExpect(status().isOk());

        var productsCache = cacheManager.getCache("products");
        assertThat(productsCache).isNotNull();
        assertThat(productsCache.get(id)).isNotNull();

        // Delete via service — should evict cache
        mockMvc.perform(delete("/api/products/" + id))
                .andExpect(status().isNoContent());

        assertThat(productsCache.get(id)).isNull();
    }

    @Test
    @DisplayName("should update cache after product update")
    void shouldUpdateCacheAfterProductUpdate() throws Exception {
        long id = createProduct(MACBOOK);

        // Populate cache
        mockMvc.perform(get("/api/products/" + id))
                .andExpect(status().isOk());

        ProductRequestDTO updated = new ProductRequestDTO(
                "MacBook Pro M3", "Electronics", "MBP-001", new BigDecimal("1799.99"), "Updated"
        );

        // Update — should refresh cache via @CachePut
        mockMvc.perform(put("/api/products/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk());

        // Delete from DB directly — cache should have updated version
        productRepository.deleteAll();

        mockMvc.perform(get("/api/products/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("MacBook Pro M3"))
                .andExpect(jsonPath("$.price").value(1799.99));
    }
}