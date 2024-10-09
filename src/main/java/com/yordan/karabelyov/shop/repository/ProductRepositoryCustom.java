package com.yordan.karabelyov.shop.repository;

import com.yordan.karabelyov.shop.repository.impl.criteria.holder.ProductCriteriaHolder;

public interface ProductRepositoryCustom {
    Integer count(ProductCriteriaHolder productCriteriaHolder);
}
