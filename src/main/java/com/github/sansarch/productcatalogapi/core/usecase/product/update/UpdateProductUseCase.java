package com.github.sansarch.productcatalogapi.core.usecase.product.update;

import com.github.sansarch.productcatalogapi.core.model.product.Product;
import com.github.sansarch.productcatalogapi.core.model.product.vo.ProductId;
import com.github.sansarch.productcatalogapi.core.usecase.UseCase;

public interface UpdateProductUseCase extends UseCase {

    Product execute(ProductId id, Product updated);
}
