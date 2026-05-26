package com.github.sansarch.productcatalogapi.core.usecase.product.delete;

import com.github.sansarch.productcatalogapi.core.model.product.vo.ProductId;
import com.github.sansarch.productcatalogapi.core.usecase.UseCase;

public interface DeleteProductUseCase extends UseCase {

    void execute(ProductId id);
}
