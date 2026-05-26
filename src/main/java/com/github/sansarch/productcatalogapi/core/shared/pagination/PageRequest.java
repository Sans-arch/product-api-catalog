package com.github.sansarch.productcatalogapi.core.shared.pagination;

public record PageRequest(int page, int size, String sort) {
    public PageRequest {
        if (page < 0) throw new IllegalArgumentException("Page must be >= 0");
        if (size < 1) throw new IllegalArgumentException("Size must be >= 1");
    }

    public static PageRequest of(int page, int size) {
        return new PageRequest(page, size, null);
    }

    public static PageRequest of(int page, int size, String sort) {
        return new PageRequest(page, size, sort);
    }
}
