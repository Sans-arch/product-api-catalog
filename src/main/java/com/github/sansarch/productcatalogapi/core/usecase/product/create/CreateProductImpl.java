package com.github.sansarch.productcatalogapi.core.usecase.product.create;

import com.github.sansarch.productcatalogapi.core.exception.DuplicateSkuException;
import com.github.sansarch.productcatalogapi.core.model.product.Product;
import com.github.sansarch.productcatalogapi.core.repository.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class CreateProductImpl implements CreateProductUseCase {

    private final ProductRepository repository;

    public CreateProductImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product execute(Product product) {
        if (repository.findBySku(product.getSku()).isPresent()) {
            throw new DuplicateSkuException(product.getSku());
        }

        return repository.save(product);
    }
}
