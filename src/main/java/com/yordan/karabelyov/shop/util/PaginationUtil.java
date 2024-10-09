package com.yordan.karabelyov.shop.util;

public class PaginationUtil {
    public static PaginationInformation constructPaginationHolder(int pageNumber, int pageSize, long totalCount) {
        return new PaginationInformation(pageNumber, pageSize, totalCount);
    }

    public static int calculateStartingResult(int pageNumber, int pageSize) {
        if (pageNumber < 1) {
            throw new IllegalArgumentException(Constants.PAGE_NUMBER_MIN_SIZE);
        }
        if (pageSize < 1) {
            throw new IllegalArgumentException(Constants.PAGE_SIZE_MIN_SIZE);
        }
        return (pageNumber - 1) * pageSize;
    }
}

