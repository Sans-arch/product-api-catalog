package com.github.sansarch.productcatalogapi.domain.repository;

import com.github.sansarch.productcatalogapi.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByCategory(String category, Pageable pageable);

    Optional<Product> findBySku(String sku);

    Page<Product> findByCategoryAndPriceLessThan(String category, BigDecimal maxPrice, Pageable pageable);
}
