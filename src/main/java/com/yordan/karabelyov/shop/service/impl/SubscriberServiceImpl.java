package com.yordan.karabelyov.shop.service.impl;

import com.yordan.karabelyov.shop.entity.Product;
import com.yordan.karabelyov.shop.entity.Subscriber;
import com.yordan.karabelyov.shop.exception.NotFoundException;
import com.yordan.karabelyov.shop.repository.ProductRepository;
import com.yordan.karabelyov.shop.repository.SubscriberRepository;
import com.yordan.karabelyov.shop.repository.impl.criteria.holder.SubscriberCriteriaHolder;
import com.yordan.karabelyov.shop.service.SubscriberService;
import com.yordan.karabelyov.shop.util.ErrorMessages;
import com.yordan.karabelyov.shop.util.Page;
import com.yordan.karabelyov.shop.util.PaginationInformation;
import com.yordan.karabelyov.shop.util.PaginationUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubscriberServiceImpl implements SubscriberService {
    SubscriberRepository subscriberRepository;

    ProductRepository productRepository;

    public SubscriberServiceImpl(SubscriberRepository subscriberRepository, ProductRepository productRepository) {
        this.subscriberRepository = subscriberRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void validateSubscriberExistence(Long subscriberId) {
        if (!subscriberRepository.existsById(subscriberId))
            throw new NotFoundException(String.format(ErrorMessages.SUBSCRIBER_GET_NOT_FOUND, subscriberId));
    }

    @Override
    public Subscriber create(Subscriber subscriber) {
        return subscriberRepository.save(subscriber);
    }

    @Override
    public Subscriber getSubscriberById(Long subscriberId) {
        return subscriberRepository.findById(subscriberId)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessages.SUBSCRIBER_GET_NOT_FOUND, subscriberId)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Subscriber> subscriberPagination(SubscriberCriteriaHolder holder) {

        PaginationInformation paginationInformation = PaginationUtil.constructPaginationHolder(
                holder.getPageNumber(),
                holder.getPageSize(),
                subscriberRepository.count(holder)
        );

        List<Subscriber> fetchedSubscribers = subscriberRepository.findAllByCriteria(holder);

        return new Page<>(paginationInformation, fetchedSubscribers);
    }

    @Override
    public Subscriber update(Subscriber updateSubscriber, Long subscriberId) {
        validateSubscriberExistence(subscriberId);

        Subscriber existingSubscriber = subscriberRepository.findById(subscriberId).get();
        existingSubscriber.setFirstName(updateSubscriber.getFirstName());
        existingSubscriber.setLastName(updateSubscriber.getLastName());

        return subscriberRepository.save(existingSubscriber);
    }

    @Override
    @Transactional
    public void deleteSubscriber(Long subscriberId) {
        Subscriber subscriber = subscriberRepository.findById(subscriberId)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessages.SUBSCRIBER_GET_NOT_FOUND, subscriberId)));

        subscriber.getProducts().remove(subscriber);

        subscriberRepository.delete(subscriber);
    }

    @Override
    @Transactional
    public void addProducts(Long subscriberId, List<Long> productIds) {
        Subscriber subscriber = getSubscriberById(subscriberId);
        List<Product> products = getProductsById(productIds);

        subscriber.getProducts().addAll(products);
        products.forEach(product -> product.getProductSubscribers().add(subscriber));

        subscriberRepository.saveAndFlush(subscriber);
    }

    @Override
    @Transactional
    public void removeProducts(Long subscriberId, List<Long> productIds) {
        Subscriber subscriber = getSubscriberById(subscriberId);
        List<Product> productsToRemove  = getProductsById(productIds);

        subscriber.getProducts().removeAll(productsToRemove);
        productsToRemove.forEach(product -> product.getProductSubscribers().remove(subscriber));

        subscriberRepository.saveAndFlush(subscriber);
    }

    private List<Product> getProductsById(List<Long> productIds) {
        List<Product> fetchedProducts = new ArrayList<>();

        for (Long productId : productIds) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new NotFoundException(ErrorMessages.PRODUCT_NOT_FOUND));
            fetchedProducts.add(product);
        }

        return fetchedProducts;
    }
}
