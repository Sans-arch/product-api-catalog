package com.github.sansarch.productcatalogapi.core.usecase.product.list;

import com.github.sansarch.productcatalogapi.core.model.product.Product;
import com.github.sansarch.productcatalogapi.core.repository.ProductRepository;
import com.github.sansarch.productcatalogapi.core.shared.pagination.PageRequest;
import com.github.sansarch.productcatalogapi.core.shared.pagination.PageResult;
import org.springframework.stereotype.Component;

@Component
public class ListProductsImpl implements ListProductsUseCase {

    private final ProductRepository repository;

    public ListProductsImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public PageResult<Product> execute(PageRequest pageRequest) {
        return repository.findAll(pageRequest);
    }
}
