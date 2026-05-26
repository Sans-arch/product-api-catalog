package com.github.sansarch.productcatalogapi.core.usecase.product.listbycategory;

import com.github.sansarch.productcatalogapi.core.model.product.Product;
import com.github.sansarch.productcatalogapi.core.repository.ProductRepository;
import com.github.sansarch.productcatalogapi.core.shared.pagination.PageRequest;
import com.github.sansarch.productcatalogapi.core.shared.pagination.PageResult;
import org.springframework.stereotype.Component;

@Component
public class ListProductsByCategoryImpl implements ListProductsByCategoryUseCase {

    private final ProductRepository repository;

    public ListProductsByCategoryImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public PageResult<Product> execute(String category, PageRequest pageRequest) {
        return repository.findByCategory(category, pageRequest);
    }
}
