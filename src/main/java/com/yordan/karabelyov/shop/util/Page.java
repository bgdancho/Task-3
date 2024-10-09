package com.yordan.karabelyov.shop.util;

import java.util.List;

public class Page<T> {
    private final PaginationInformation paginationInformation;
    private final List<T> content;

    public Page(PaginationInformation paginationInformation, List<T> content) {
        this.paginationInformation = paginationInformation;
        this.content = content;
    }

    public PaginationInformation getPaginationInformation() {
        return paginationInformation;
    }

    public List<T> getContent() {
        return content;
    }
}
