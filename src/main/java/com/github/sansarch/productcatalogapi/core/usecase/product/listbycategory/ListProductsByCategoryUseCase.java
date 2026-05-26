package com.github.sansarch.productcatalogapi.core.usecase.product.listbycategory;

import com.github.sansarch.productcatalogapi.core.model.product.Product;
import com.github.sansarch.productcatalogapi.core.shared.pagination.PageRequest;
import com.github.sansarch.productcatalogapi.core.shared.pagination.PageResult;
import com.github.sansarch.productcatalogapi.core.usecase.UseCase;

public interface ListProductsByCategoryUseCase extends UseCase {

    PageResult<Product> execute(String category, PageRequest pageRequest);
}
