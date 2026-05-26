package com.github.sansarch.productcatalogapi.core.usecase.product.find;

import com.github.sansarch.productcatalogapi.core.exception.ProductNotFoundException;
import com.github.sansarch.productcatalogapi.core.model.product.Product;
import com.github.sansarch.productcatalogapi.core.model.product.vo.ProductId;
import com.github.sansarch.productcatalogapi.core.repository.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class FindProductImpl implements FindProductUseCase {

    private final ProductRepository repository;

    public FindProductImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product byId(ProductId id) {
        return repository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id.value()));
    }

    @Override
    public Product bySku(String sku) {
        return repository.findBySku(sku)
            .orElseThrow(() -> new ProductNotFoundException(sku));
    }
}
