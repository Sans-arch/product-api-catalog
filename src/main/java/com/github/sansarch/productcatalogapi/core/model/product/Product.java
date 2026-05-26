package com.github.sansarch.productcatalogapi.core.model.product;

import com.github.sansarch.productcatalogapi.core.model.product.vo.ProductId;

import java.math.BigDecimal;

public final class Product {

    private final ProductId id;
    private String name;
    private String category;
    private String sku;
    private BigDecimal price;
    private String description;

    public Product(ProductId id, String name, String category, String sku, BigDecimal price, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.sku = sku;
        this.price = price;
        this.description = description;
    }

    public Product(String name, String category, String sku, BigDecimal price, String description) {
        this.id = ProductId.generate();
        this.name = name;
        this.category = category;
        this.sku = sku;
        this.price = price;
        this.description = description;
    }

    public ProductId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getSku() {
        return sku;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changePrice(BigDecimal price) {
        this.price = price;
    }

    public void changeCategory(String category) {
        this.category = category;
    }

    public void changeSku(String sku) {
        this.sku = sku;
    }
}
