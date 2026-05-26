package com.github.sansarch.productcatalogapi.infrastructure.adapters.in.dtos;

import com.github.sansarch.productcatalogapi.core.shared.pagination.PageResult;

import java.util.List;
import java.util.function.Function;

public record PageResponseDTO<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
    public static <S, T> PageResponseDTO<T> from(PageResult<S> pageResult, Function<S, T> mapper) {
        return new PageResponseDTO<>(
                pageResult.content().stream().map(mapper).toList(),
                pageResult.page(),
                pageResult.size(),
                pageResult.totalElements(),
                pageResult.totalPages(),
                !pageResult.hasPrevious(),
                !pageResult.hasNext()
        );
    }
}
