package com.github.sansarch.productcatalogapi.core.usecase.product.create;

import com.github.sansarch.productcatalogapi.core.model.product.Product;
import com.github.sansarch.productcatalogapi.core.usecase.UseCase;

public interface CreateProductUseCase extends UseCase {

    Product execute(Product product);
}
