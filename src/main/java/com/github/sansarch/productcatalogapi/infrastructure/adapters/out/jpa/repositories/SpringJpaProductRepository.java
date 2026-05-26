package com.github.sansarch.productcatalogapi.infrastructure.adapters.out.jpa.repositories;

import com.github.sansarch.productcatalogapi.infrastructure.adapters.out.jpa.entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface SpringJpaProductRepository extends JpaRepository<ProductEntity, UUID> {

    Page<ProductEntity> findByCategory(String category, Pageable pageable);
    Optional<ProductEntity> findBySku(String sku);
    Page<ProductEntity> findByCategoryAndPriceLessThan(String category, BigDecimal maxPrice, Pageable pageable);
}
