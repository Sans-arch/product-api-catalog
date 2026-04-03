package com.github.sansarch.productcatalogapi.domain.service;

import com.github.sansarch.productcatalogapi.domain.entity.Product;
import com.github.sansarch.productcatalogapi.domain.exception.DuplicateSkuException;
import com.github.sansarch.productcatalogapi.domain.exception.ProductNotFoundException;
import com.github.sansarch.productcatalogapi.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductQueryService queryService;

    @InjectMocks
    private ProductService service;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("MacBook Pro");
        product.setSku("MBP-001");
        product.setCategory("Electronics");
        product.setPrice(new BigDecimal("1999.99"));
        product.setDescription("Apple M3 chip");
    }

    @Test
    @DisplayName("should return product when ID exists")
    void shouldReturnProductWhenIdExists() {
        when(queryService.getById(1L)).thenReturn(product);

        Product result = service.getById(1L);

        assertThat(result.getName()).isEqualTo("MacBook Pro");
        assertThat(result.getSku()).isEqualTo("MBP-001");
        verify(queryService, times(1)).getById(1L);
    }

    @Test
    @DisplayName("should throw ProductNotFoundException when ID does not exist")
    void shouldThrowProductNotFoundExceptionWhenIdDoesNotExist() {
        when(queryService.getById(999L)).thenThrow(new ProductNotFoundException(999L));

        assertThatThrownBy(() -> service.getById(999L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    @DisplayName("should persist product when SKU is unique")
    void shouldPersistProductWhenSkuIsUnique() {
        when(repository.findBySku("MBP-001")).thenReturn(Optional.empty());
        when(repository.save(product)).thenReturn(product);

        Product result = service.save(product);

        assertThat(result.getSku()).isEqualTo("MBP-001");
        verify(repository, times(1)).save(product);
    }

    @Test
    @DisplayName("should throw DuplicateSkuException when SKU already exists")
    void shouldThrowDuplicateSkuExceptionWhenSkuAlreadyExists() {
        when(repository.findBySku("MBP-001")).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> service.save(product))
                .isInstanceOf(DuplicateSkuException.class)
                .hasMessageContaining("MBP-001");

        verify(repository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("should return matching products when category exists")
    void shouldReturnMatchingProductsWhenCategoryExists() {
        when(queryService.getByCategory("Electronics")).thenReturn(List.of(product));

        List<Product> results = service.getByCategory("Electronics");

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getCategory()).isEqualTo("Electronics");
        verify(queryService, times(1)).getByCategory("Electronics");
    }

    @Test
    @DisplayName("should call repository when deleting product")
    void shouldCallRepositoryWhenDeletingProduct() {
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("should throw ProductNotFoundException when updating non-existing product")
    void shouldThrowProductNotFoundExceptionWhenUpdatingNonExistingProduct() {
        when(queryService.getById(999L)).thenThrow(new ProductNotFoundException(999L));

        assertThatThrownBy(() -> service.update(999L, product))
                .isInstanceOf(ProductNotFoundException.class);
    }

    // ── getAllProducts ────────────────────────────────────────────────────────

    @Test
    @DisplayName("should return all products")
    void shouldReturnAllProducts() {
        Product other = new Product();
        other.setId(2L);
        other.setName("iPhone 15");
        other.setSku("IPH-015");
        other.setCategory("Electronics");
        other.setPrice(new BigDecimal("999.99"));

        when(repository.findAll()).thenReturn(List.of(product, other));

        List<Product> results = service.getAllProducts();

        assertThat(results).hasSize(2);
        verify(repository, times(1)).findAll();
    }

    // ── getBySku ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("should return product when SKU exists")
    void shouldReturnProductWhenSkuExists() {
        when(queryService.getBySku("MBP-001")).thenReturn(product);

        Product result = service.getBySku("MBP-001");

        assertThat(result.getSku()).isEqualTo("MBP-001");
        assertThat(result.getName()).isEqualTo("MacBook Pro");
        verify(queryService, times(1)).getBySku("MBP-001");
    }

    @Test
    @DisplayName("should throw ProductNotFoundException when SKU does not exist")
    void shouldThrowProductNotFoundExceptionWhenSkuDoesNotExist() {
        when(queryService.getBySku("UNKNOWN")).thenThrow(new ProductNotFoundException("UNKNOWN"));

        assertThatThrownBy(() -> service.getBySku("UNKNOWN"))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("UNKNOWN");
    }

    // ── getByCategoryUnderPrice ───────────────────────────────────────────────

    @Test
    @DisplayName("should return products when category and max price match")
    void shouldReturnProductsWhenCategoryAndMaxPriceMatch() {
        when(queryService.getByCategoryUnderPrice("Electronics", 2500.0))
                .thenReturn(List.of(product));

        List<Product> results = service.getByCategoryUnderPrice("Electronics", 2500.0);

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getCategory()).isEqualTo("Electronics");
        verify(queryService, times(1)).getByCategoryUnderPrice("Electronics", 2500.0);
    }

    // ── update ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("should update and return product when ID exists and SKU is unique")
    void shouldUpdateAndReturnProductWhenIdExistsAndSkuIsUnique() {
        Product updated = new Product();
        updated.setName("MacBook Pro 16");
        updated.setSku("MBP-016");
        updated.setCategory("Electronics");
        updated.setPrice(new BigDecimal("2499.99"));
        updated.setDescription("Apple M3 Max chip");

        when(queryService.getById(1L)).thenReturn(product);
        when(repository.findBySku("MBP-016")).thenReturn(Optional.empty());
        when(repository.save(product)).thenReturn(product);

        Product result = service.update(1L, updated);

        assertThat(result.getName()).isEqualTo("MacBook Pro 16");
        assertThat(result.getSku()).isEqualTo("MBP-016");
        verify(repository, times(1)).save(product);
    }

    @Test
    @DisplayName("should throw DuplicateSkuException when updating to an already used SKU")
    void shouldThrowDuplicateSkuExceptionWhenUpdatingToAlreadyUsedSku() {
        Product conflicting = new Product();
        conflicting.setId(2L);
        conflicting.setSku("CONFLICT-SKU");

        Product updated = new Product();
        updated.setName("MacBook Pro 16");
        updated.setSku("CONFLICT-SKU");
        updated.setCategory("Electronics");
        updated.setPrice(new BigDecimal("2499.99"));

        when(queryService.getById(1L)).thenReturn(product);
        when(repository.findBySku("CONFLICT-SKU")).thenReturn(Optional.of(conflicting));

        assertThatThrownBy(() -> service.update(1L, updated))
                .isInstanceOf(DuplicateSkuException.class)
                .hasMessageContaining("CONFLICT-SKU");

        verify(repository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("should update product successfully when SKU is unchanged")
    void shouldUpdateProductSuccessfullyWhenSkuIsUnchanged() {
        Product updated = new Product();
        updated.setName("MacBook Pro Updated");
        updated.setSku("MBP-001"); // same SKU as existing
        updated.setCategory("Electronics");
        updated.setPrice(new BigDecimal("1899.99"));
        updated.setDescription("Updated description");

        when(queryService.getById(1L)).thenReturn(product);
        when(repository.save(product)).thenReturn(product);

        Product result = service.update(1L, updated);

        assertThat(result.getName()).isEqualTo("MacBook Pro Updated");
        // findBySku should NOT be called on repository because the SKU didn't change
        verify(repository, never()).findBySku(anyString());
        verify(repository, times(1)).save(product);
    }
}
