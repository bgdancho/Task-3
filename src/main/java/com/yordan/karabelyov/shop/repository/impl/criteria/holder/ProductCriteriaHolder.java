package com.yordan.karabelyov.shop.repository.impl.criteria.holder;

import com.yordan.karabelyov.shop.enumeration.ProductOrderBy;
import com.yordan.karabelyov.shop.enumeration.SortOrder;
import com.yordan.karabelyov.shop.repository.CriteriaRepository;

import java.util.List;

public class ProductCriteriaHolder implements CriteriaRepository.ParamHolder {

    private List<Long> subscriberIds;
    private Integer pageNumber;
    private Integer pageSize;
    private ProductOrderBy productOrderBy;
    private SortOrder sortOrder;

    private ProductCriteriaHolder(Builder builder) {
        this.subscriberIds = builder.subscriberIds;
        this.pageNumber = builder.pageNumber;
        this.pageSize = builder.pageSize;
        this.productOrderBy = builder.productOrderBy;
    }

    public List<Long> getSubscriberIds() {
        return subscriberIds;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public ProductOrderBy getProductOrderBy() {
        return productOrderBy;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public static class Builder {
        private List<Long> subscriberIds;
        private Integer pageNumber;
        private Integer pageSize;
        private ProductOrderBy productOrderBy;

        private SortOrder sortOrder;

        public Builder withSubscribersIds(List<Long> subscriberIds) {
            this.subscriberIds = subscriberIds;
            return this;
        }

        public Builder withPageNumber(Integer pageNumber) {
            this.pageNumber = pageNumber;
            return this;
        }

        public Builder withPageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder withSortOrder(SortOrder sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public Builder withOrderBy(ProductOrderBy productOrderBy) {
            this.productOrderBy = productOrderBy;
            return this;
        }

        public ProductCriteriaHolder build() {
            return new ProductCriteriaHolder(this);
        }
    }
}
