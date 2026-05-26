package com.github.sansarch.productcatalogapi.core.usecase.product.update;

import com.github.sansarch.productcatalogapi.core.exception.DuplicateSkuException;
import com.github.sansarch.productcatalogapi.core.model.product.Product;
import com.github.sansarch.productcatalogapi.core.model.product.vo.ProductId;
import com.github.sansarch.productcatalogapi.core.repository.ProductRepository;
import com.github.sansarch.productcatalogapi.core.usecase.product.find.FindProductUseCase;
import org.springframework.stereotype.Component;

@Component
public class UpdateProductImpl implements UpdateProductUseCase {

    private final ProductRepository repository;
    private final FindProductUseCase findProductUseCase;

    public UpdateProductImpl(ProductRepository repository, FindProductUseCase findProductUseCase) {
        this.repository = repository;
        this.findProductUseCase = findProductUseCase;
    }

    @Override
    public Product execute(ProductId id, Product updated) {
        Product existing = findProductUseCase.byId(id);

        if (!existing.getSku().equals(updated.getSku()) && repository.findBySku(updated.getSku()).isPresent()) {
            throw new DuplicateSkuException(updated.getSku());
        }

        existing.changeName(updated.getName());
        existing.changeDescription(updated.getDescription());
        existing.changePrice(updated.getPrice());
        existing.changeCategory(updated.getCategory());
        existing.changeSku(updated.getSku());

        return repository.save(existing);
    }
}
