package com.yordan.karabelyov.shop.service;

import com.yordan.karabelyov.shop.entity.Subscriber;

import com.yordan.karabelyov.shop.repository.impl.criteria.holder.SubscriberCriteriaHolder;
import com.yordan.karabelyov.shop.util.Page;

import java.util.List;

public interface SubscriberService {
    void validateSubscriberExistence(Long subscriberId);

    Subscriber create(Subscriber subscriber);

    Subscriber getSubscriberById(Long id);

    Page<Subscriber> subscriberPagination(SubscriberCriteriaHolder params);

    Subscriber update(Subscriber updateSubscriber, Long id);

    void deleteSubscriber(Long id);

    void addProducts(Long subscriberId, List<Long> productIds);

    void removeProducts(Long subscriberId, List<Long> productIds);
}
