package com.github.sansarch.productcatalogapi.core.usecase.product.listbycategoryunderprice;

import com.github.sansarch.productcatalogapi.core.model.product.Product;
import com.github.sansarch.productcatalogapi.core.shared.pagination.PageRequest;
import com.github.sansarch.productcatalogapi.core.shared.pagination.PageResult;
import com.github.sansarch.productcatalogapi.core.usecase.UseCase;

import java.math.BigDecimal;

public interface ListByCategoryUnderPriceUseCase extends UseCase {

    PageResult<Product> execute(String category, BigDecimal maxPrice, PageRequest pageRequest);
}
