package com.github.sansarch.productcatalogapi.core.usecase.product.find;

import com.github.sansarch.productcatalogapi.core.model.product.Product;
import com.github.sansarch.productcatalogapi.core.model.product.vo.ProductId;
import com.github.sansarch.productcatalogapi.core.usecase.UseCase;

public interface FindProductUseCase extends UseCase {

    Product byId(ProductId id);
    Product bySku(String sku);
}
