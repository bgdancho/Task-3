package com.yordan.karabelyov.shop.repository;

import com.yordan.karabelyov.shop.repository.impl.criteria.holder.SubscriberCriteriaHolder;

public interface SubscriberRepositoryCustom {
    Integer count(SubscriberCriteriaHolder subscriberCriteriaHolder);
}
