package com.github.sansarch.productcatalogapi.infrastructure.adapters.out.jpa.mappers;

import com.github.sansarch.productcatalogapi.core.shared.pagination.PageRequest;
import com.github.sansarch.productcatalogapi.core.shared.pagination.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.function.Function;

public class PageMapper {

    private PageMapper() {
    }

    public static org.springframework.data.domain.PageRequest toSpringPageRequest(PageRequest pageRequest) {
        if (pageRequest.sort() != null && !pageRequest.sort().isBlank()) {
            return org.springframework.data.domain.PageRequest.of(
                pageRequest.page(),
                pageRequest.size(),
                Sort.by(pageRequest.sort())
            );
        }

        return org.springframework.data.domain.PageRequest.of(
            pageRequest.page(),
            pageRequest.size()
        );
    }

    public static <T, R> PageResult<R> toPageResult(Page<T> page, Function<T, R> mapper) {
        return PageResult.of(
            page.getContent().stream().map(mapper).toList(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements()
        );
    }
}
