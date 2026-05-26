package com.github.sansarch.productcatalogapi.infrastructure.adapters.out.jpa;

import com.github.sansarch.productcatalogapi.core.model.product.Product;
import com.github.sansarch.productcatalogapi.core.model.product.vo.ProductId;
import com.github.sansarch.productcatalogapi.core.repository.ProductRepository;
import com.github.sansarch.productcatalogapi.core.shared.pagination.PageRequest;
import com.github.sansarch.productcatalogapi.core.shared.pagination.PageResult;
import com.github.sansarch.productcatalogapi.infrastructure.adapters.out.jpa.entities.ProductEntity;
import com.github.sansarch.productcatalogapi.infrastructure.adapters.out.jpa.mappers.ProductEntityMapper;
import com.github.sansarch.productcatalogapi.infrastructure.adapters.out.jpa.repositories.SpringJpaProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

import static com.github.sansarch.productcatalogapi.infrastructure.adapters.out.jpa.mappers.PageMapper.toPageResult;
import static com.github.sansarch.productcatalogapi.infrastructure.adapters.out.jpa.mappers.PageMapper.toSpringPageRequest;

@Repository
public class JpaProductRepositoryAdapter implements ProductRepository {

    private final SpringJpaProductRepository springJpaProductRepository;

    public JpaProductRepositoryAdapter(SpringJpaProductRepository springJpaProductRepository) {
        this.springJpaProductRepository = springJpaProductRepository;
    }

    @Override
    @Caching(
        put = @CachePut(value = "products", key = "#result.id.value()"),
        evict = @CacheEvict(value = "products-by-category", allEntries = true)
    )
    public Product save(Product product) {
        ProductEntity entity = ProductEntityMapper.toEntity(product);
        ProductEntity saved = springJpaProductRepository.save(entity);
        return ProductEntityMapper.toDomain(saved);
    }

    @Override
    @Cacheable(value = "products", key = "#id.value()")
    public Optional<Product> findById(ProductId id) {
        return springJpaProductRepository.findById(id.value())
            .map(ProductEntityMapper::toDomain);
    }

    @Override
    @Cacheable(value = "products", key = "#sku")
    public Optional<Product> findBySku(String sku) {
        return springJpaProductRepository.findBySku(sku)
            .map(ProductEntityMapper::toDomain);
    }

    @Override
    public PageResult<Product> findAll(PageRequest pageRequest) {
        return toPageResult(
            springJpaProductRepository.findAll(toSpringPageRequest(pageRequest)),
            ProductEntityMapper::toDomain
        );
    }

    @Override
    public PageResult<Product> findByCategory(String category, PageRequest pageRequest) {
        return toPageResult(
            springJpaProductRepository.findByCategory(category, toSpringPageRequest(pageRequest)),
            ProductEntityMapper::toDomain
        );
    }

    @Override
    @Cacheable(
        value = "products-by-category",
        key = "#category + '-' + #maxPrice + '-' + #pageRequest.page() + '-' + #pageRequest.size() + '-' + #pageRequest.sort()"
    )
    public PageResult<Product> findByCategoryAndPriceLessThan(
        String category,
        BigDecimal maxPrice,
        PageRequest pageRequest
    ) {
        return toPageResult(
            springJpaProductRepository.findByCategoryAndPriceLessThan(category, maxPrice, toSpringPageRequest(pageRequest)),
            ProductEntityMapper::toDomain);
    }

    @Override
    @Caching(evict = {
        @CacheEvict(value = "products", key = "#id.value()"),
        @CacheEvict(value = "products-by-category", allEntries = true)
    })
    public void delete(ProductId id) {
        springJpaProductRepository.deleteById(id.value());
    }
}
