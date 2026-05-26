package com.github.sansarch.productcatalogapi.core.usecase.product.listbycategoryunderprice;

import com.github.sansarch.productcatalogapi.core.model.product.Product;
import com.github.sansarch.productcatalogapi.core.repository.ProductRepository;
import com.github.sansarch.productcatalogapi.core.shared.pagination.PageRequest;
import com.github.sansarch.productcatalogapi.core.shared.pagination.PageResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ListByCategoryUnderPriceImpl implements ListByCategoryUnderPriceUseCase {

    private final ProductRepository repository;

    public ListByCategoryUnderPriceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public PageResult<Product> execute(String category, BigDecimal maxPrice, PageRequest pageRequest) {
        return repository.findByCategoryAndPriceLessThan(category, maxPrice, pageRequest);
    }
}
