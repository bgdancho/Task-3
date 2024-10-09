package com.yordan.karabelyov.shop.util;

public class PaginationInformation {
    private final int pageNumber;
    private final int pageSize;
    private final long totalItems;
    private final int totalPages;

    public PaginationInformation(int pageNumber, int pageSize, long totalItems) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.totalPages = (int) Math.ceil((double) totalItems / pageSize);
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
