package com.github.sansarch.productcatalogapi.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "products",
        indexes = {
                // Single-column index: fast lookups by category
                @Index(name = "idx_product_category", columnList = "category"),

                // Composite index: fast when filtering by BOTH category and price
                @Index(name = "idx_product_category_price", columnList = "category, price"),

                // Unique index: enforces uniqueness AND speeds up lookups by sku
                @Index(name = "idx_product_sku", columnList = "sku", unique = true)
        }
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(unique = true, nullable = false)
    private String sku;

    @Column(nullable = false)
    private Double price;

    @Column(length = 1000)
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
