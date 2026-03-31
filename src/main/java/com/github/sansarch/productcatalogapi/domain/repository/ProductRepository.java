package com.github.sansarch.productcatalogapi.domain.repository;

import com.github.sansarch.productcatalogapi.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Uses idx_product category
    List<Product> findByCategory(String category);

    // Uses idx_product_sku
    Optional<Product> findBySku(String sku);

    // Uses idx_product_category_price (composite index)
    List<Product> findByCategoryAndPriceLessThan(String category, Double maxPrice);

    // Custom JPQL query -- still uses the category index
    @Query("SELECT p FROM Product p WHERE p.category = :category ORDER BY p.price ASC")
    List<Product> findByCategoryOrderByPrice(@Param("category") String category);
}
