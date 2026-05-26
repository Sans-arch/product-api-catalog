package com.github.sansarch.productcatalogapi.core.repository;

import com.github.sansarch.productcatalogapi.core.model.product.Product;
import com.github.sansarch.productcatalogapi.core.model.product.vo.ProductId;
import com.github.sansarch.productcatalogapi.core.shared.pagination.PageRequest;
import com.github.sansarch.productcatalogapi.core.shared.pagination.PageResult;

import java.math.BigDecimal;
import java.util.Optional;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(ProductId id);

    Optional<Product> findBySku(String sku);

    PageResult<Product> findAll(PageRequest pageRequest);

    PageResult<Product> findByCategory(String category, PageRequest pageRequest);

    PageResult<Product> findByCategoryAndPriceLessThan(String category, BigDecimal maxPrice, PageRequest pageRequest);

    void delete(ProductId id);
}
