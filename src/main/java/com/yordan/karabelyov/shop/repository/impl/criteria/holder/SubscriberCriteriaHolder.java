package com.yordan.karabelyov.shop.repository.impl.criteria.holder;

import com.yordan.karabelyov.shop.enumeration.SortOrder;
import com.yordan.karabelyov.shop.enumeration.SubscriberOrderBy;
import com.yordan.karabelyov.shop.repository.CriteriaRepository;

import java.util.List;

public class SubscriberCriteriaHolder implements CriteriaRepository.ParamHolder {
    private List<Long> productIds;
    private Integer pageNumber;
    private Integer pageSize;
    private SubscriberOrderBy subscriberOrderBy;
    private SortOrder sortOrder;

    public SubscriberCriteriaHolder(Builder builder) {
        this.productIds = builder.productIds;
        this.pageNumber = builder.pageNumber;
        this.pageSize = builder.pageSize;
        this.subscriberOrderBy = builder.subscriberOrderBy;
        this.sortOrder = builder.sortOrder;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public SubscriberOrderBy getSubscriberOrderBy() {
        return subscriberOrderBy;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public static class Builder {
        private List<Long> productIds;
        private Integer pageNumber;
        private Integer pageSize;
        private SubscriberOrderBy subscriberOrderBy;

        private SortOrder sortOrder;

        public SubscriberCriteriaHolder.Builder withProductsIds(List<Long> productIds) {
            this.productIds = productIds;
            return this;
        }

        public SubscriberCriteriaHolder.Builder withPageNumber(Integer pageNumber) {
            this.pageNumber = pageNumber;
            return this;
        }

        public SubscriberCriteriaHolder.Builder withPageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public SubscriberCriteriaHolder.Builder withSortOrder(SortOrder sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public SubscriberCriteriaHolder.Builder withOrderBy(SubscriberOrderBy subscriberOrderBy) {
            this.subscriberOrderBy = subscriberOrderBy;
            return this;
        }

        public SubscriberCriteriaHolder build() {
            return new SubscriberCriteriaHolder(this);
        }
    }
}
