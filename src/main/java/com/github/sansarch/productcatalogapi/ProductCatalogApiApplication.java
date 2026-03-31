package com.github.sansarch.productcatalogapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ProductCatalogApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductCatalogApiApplication.class, args);
    }

}
