package com.github.sansarch.productcatalogapi.core.usecase.product.delete;

import com.github.sansarch.productcatalogapi.core.model.product.vo.ProductId;
import com.github.sansarch.productcatalogapi.core.repository.ProductRepository;
import com.github.sansarch.productcatalogapi.core.usecase.product.find.FindProductUseCase;
import org.springframework.stereotype.Component;

@Component
public class DeleteProductImpl implements DeleteProductUseCase {

    private final ProductRepository repository;
    private final FindProductUseCase findProductUseCase;

    public DeleteProductImpl(ProductRepository repository, FindProductUseCase findProductUseCase) {
        this.repository = repository;
        this.findProductUseCase = findProductUseCase;
    }

    @Override
    public void execute(ProductId id) {
        findProductUseCase.byId(id);
        repository.delete(id);
    }
}
