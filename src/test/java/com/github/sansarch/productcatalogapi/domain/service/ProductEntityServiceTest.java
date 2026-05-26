package com.github.sansarch.productcatalogapi.domain.service;

import com.github.sansarch.productcatalogapi.core.exception.DuplicateSkuException;
import com.github.sansarch.productcatalogapi.core.exception.ProductNotFoundException;
import com.github.sansarch.productcatalogapi.core.model.product.Product;
import com.github.sansarch.productcatalogapi.core.model.product.vo.ProductId;
import com.github.sansarch.productcatalogapi.core.repository.ProductRepository;
import com.github.sansarch.productcatalogapi.core.shared.pagination.PageRequest;
import com.github.sansarch.productcatalogapi.core.shared.pagination.PageResult;
import com.github.sansarch.productcatalogapi.core.usecase.product.create.CreateProductImpl;
import com.github.sansarch.productcatalogapi.core.usecase.product.delete.DeleteProductImpl;
import com.github.sansarch.productcatalogapi.core.usecase.product.find.FindProductImpl;
import com.github.sansarch.productcatalogapi.core.usecase.product.list.ListProductsImpl;
import com.github.sansarch.productcatalogapi.core.usecase.product.listbycategory.ListProductsByCategoryImpl;
import com.github.sansarch.productcatalogapi.core.usecase.product.listbycategoryunderprice.ListByCategoryUnderPriceImpl;
import com.github.sansarch.productcatalogapi.core.usecase.product.update.UpdateProductImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductEntityServiceTest {

    @Mock
    private ProductRepository repository;

    private FindProductImpl findProduct;
    private CreateProductImpl createProduct;
    private DeleteProductImpl deleteProduct;
    private UpdateProductImpl updateProduct;
    private ListProductsImpl listProducts;
    private ListProductsByCategoryImpl listByCategory;
    private ListByCategoryUnderPriceImpl listByCategoryUnderPrice;

    private Product product;
    private ProductId productId;

    @BeforeEach
    void setUp() {
        findProduct = new FindProductImpl(repository);
        createProduct = new CreateProductImpl(repository);
        deleteProduct = new DeleteProductImpl(repository, findProduct);
        updateProduct = new UpdateProductImpl(repository, findProduct);
        listProducts = new ListProductsImpl(repository);
        listByCategory = new ListProductsByCategoryImpl(repository);
        listByCategoryUnderPrice = new ListByCategoryUnderPriceImpl(repository);

        productId = ProductId.generate();
        product = new Product(productId, "MacBook Pro", "Electronics", "MBP-001", new BigDecimal("1999.99"), "Apple M3 chip");
    }

    @Test
    @DisplayName("should return product when ID exists")
    void shouldReturnProductWhenIdExists() {
        when(repository.findById(productId)).thenReturn(Optional.of(product));

        Product result = findProduct.byId(productId);

        assertThat(result.getName()).isEqualTo("MacBook Pro");
        assertThat(result.getSku()).isEqualTo("MBP-001");
        verify(repository, times(1)).findById(productId);
    }

    @Test
    @DisplayName("should throw ProductNotFoundException when ID does not exist")
    void shouldThrowProductNotFoundExceptionWhenIdDoesNotExist() {
        ProductId unknownId = ProductId.generate();
        when(repository.findById(unknownId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> findProduct.byId(unknownId))
            .isInstanceOf(ProductNotFoundException.class)
            .hasMessageContaining(unknownId.value().toString());
    }

    @Test
    @DisplayName("should return product when SKU exists")
    void shouldReturnProductWhenSkuExists() {
        when(repository.findBySku("MBP-001")).thenReturn(Optional.of(product));

        Product result = findProduct.bySku("MBP-001");

        assertThat(result.getSku()).isEqualTo("MBP-001");
        assertThat(result.getName()).isEqualTo("MacBook Pro");
        verify(repository, times(1)).findBySku("MBP-001");
    }

    @Test
    @DisplayName("should throw ProductNotFoundException when SKU does not exist")
    void shouldThrowProductNotFoundExceptionWhenSkuDoesNotExist() {
        when(repository.findBySku("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> findProduct.bySku("UNKNOWN"))
            .isInstanceOf(ProductNotFoundException.class)
            .hasMessageContaining("UNKNOWN");
    }

    @Test
    @DisplayName("should persist product when SKU is unique")
    void shouldPersistProductWhenSkuIsUnique() {
        when(repository.findBySku("MBP-001")).thenReturn(Optional.empty());
        when(repository.save(product)).thenReturn(product);

        Product result = createProduct.execute(product);

        assertThat(result.getSku()).isEqualTo("MBP-001");
        verify(repository, times(1)).save(product);
    }

    @Test
    @DisplayName("should throw DuplicateSkuException when SKU already exists")
    void shouldThrowDuplicateSkuExceptionWhenSkuAlreadyExists() {
        when(repository.findBySku("MBP-001")).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> createProduct.execute(product))
            .isInstanceOf(DuplicateSkuException.class)
            .hasMessageContaining("MBP-001");

        verify(repository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("should delete product when it exists")
    void shouldDeleteProductWhenItExists() {
        when(repository.findById(productId)).thenReturn(Optional.of(product));

        deleteProduct.execute(productId);

        verify(repository, times(1)).delete(productId);
    }

    @Test
    @DisplayName("should throw ProductNotFoundException when deleting non-existing product")
    void shouldThrowProductNotFoundExceptionWhenDeletingNonExistingProduct() {
        ProductId unknownId = ProductId.generate();
        when(repository.findById(unknownId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteProduct.execute(unknownId))
            .isInstanceOf(ProductNotFoundException.class);

        verify(repository, never()).delete(any(ProductId.class));
    }

    @Test
    @DisplayName("should return all products")
    void shouldReturnAllProducts() {
        ProductId otherId = ProductId.generate();
        Product other = new Product(otherId, "iPhone 15", "Electronics", "IPH-015", new BigDecimal("999.99"), null);
        PageRequest pageRequest = PageRequest.of(0, 10);
        PageResult<Product> pageResult = PageResult.of(List.of(product, other), 0, 10, 2);
        when(repository.findAll(pageRequest)).thenReturn(pageResult);

        PageResult<Product> results = listProducts.execute(pageRequest);

        assertThat(results.content()).hasSize(2);
        verify(repository, times(1)).findAll(pageRequest);
    }

    @Test
    @DisplayName("should return matching products when category exists")
    void shouldReturnMatchingProductsWhenCategoryExists() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        PageResult<Product> pageResult = PageResult.of(List.of(product), 0, 10, 1);
        when(repository.findByCategory("Electronics", pageRequest)).thenReturn(pageResult);

        PageResult<Product> results = listByCategory.execute("Electronics", pageRequest);

        assertThat(results.content()).hasSize(1);
        assertThat(results.content().getFirst().getCategory()).isEqualTo("Electronics");
        verify(repository, times(1)).findByCategory("Electronics", pageRequest);
    }

    @Test
    @DisplayName("should return products when category and max price match")
    void shouldReturnProductsWhenCategoryAndMaxPriceMatch() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        PageResult<Product> pageResult = PageResult.of(List.of(product), 0, 10, 1);
        BigDecimal maxPrice = new BigDecimal("2500.00");
        when(repository.findByCategoryAndPriceLessThan("Electronics", maxPrice, pageRequest)).thenReturn(pageResult);

        PageResult<Product> results = listByCategoryUnderPrice.execute("Electronics", maxPrice, pageRequest);

        assertThat(results.content()).hasSize(1);
        assertThat(results.content().getFirst().getCategory()).isEqualTo("Electronics");
        verify(repository, times(1)).findByCategoryAndPriceLessThan("Electronics", maxPrice, pageRequest);
    }

    @Test
    @DisplayName("should update and return product when ID exists and SKU is unique")
    void shouldUpdateAndReturnProductWhenIdExistsAndSkuIsUnique() {
        Product updated = new Product(productId, "MacBook Pro 16", "Electronics", "MBP-016", new BigDecimal("2499.99"), "Apple M3 Max chip");

        when(repository.findById(productId)).thenReturn(Optional.of(product));
        when(repository.findBySku("MBP-016")).thenReturn(Optional.empty());
        when(repository.save(product)).thenReturn(product);

        Product result = updateProduct.execute(productId, updated);

        assertThat(result.getName()).isEqualTo("MacBook Pro 16");
        assertThat(result.getSku()).isEqualTo("MBP-016");
        verify(repository, times(1)).save(product);
    }

    @Test
    @DisplayName("should throw DuplicateSkuException when updating to an already used SKU")
    void shouldThrowDuplicateSkuExceptionWhenUpdatingToAlreadyUsedSku() {
        ProductId conflictId = ProductId.generate();
        Product conflicting = new Product(conflictId, "Other", "Electronics", "CONFLICT-SKU", BigDecimal.ONE, null);
        Product updated = new Product(productId, "MacBook Pro 16", "Electronics", "CONFLICT-SKU", new BigDecimal("2499.99"), null);

        when(repository.findById(productId)).thenReturn(Optional.of(product));
        when(repository.findBySku("CONFLICT-SKU")).thenReturn(Optional.of(conflicting));

        assertThatThrownBy(() -> updateProduct.execute(productId, updated))
            .isInstanceOf(DuplicateSkuException.class)
            .hasMessageContaining("CONFLICT-SKU");

        verify(repository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("should update product successfully when SKU is unchanged")
    void shouldUpdateProductSuccessfullyWhenSkuIsUnchanged() {
        Product updated = new Product(productId, "MacBook Pro Updated", "Electronics", "MBP-001", new BigDecimal("1899.99"), "Updated description");

        when(repository.findById(productId)).thenReturn(Optional.of(product));
        when(repository.save(product)).thenReturn(product);

        Product result = updateProduct.execute(productId, updated);

        assertThat(result.getName()).isEqualTo("MacBook Pro Updated");
        verify(repository, never()).findBySku(anyString());
        verify(repository, times(1)).save(product);
    }

    @Test
    @DisplayName("should throw ProductNotFoundException when updating non-existing product")
    void shouldThrowProductNotFoundExceptionWhenUpdatingNonExistingProduct() {
        ProductId unknownId = ProductId.generate();
        Product updated = new Product(unknownId, "Any", "Electronics", "ANY-SKU", BigDecimal.ONE, null);
        when(repository.findById(unknownId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateProduct.execute(unknownId, updated))
            .isInstanceOf(ProductNotFoundException.class);
    }
}
