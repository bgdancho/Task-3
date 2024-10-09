package com.yordan.karabelyov.shop.service.impl;

import com.yordan.karabelyov.shop.entity.Purchase;
import com.yordan.karabelyov.shop.entity.Product;
import com.yordan.karabelyov.shop.entity.Subscriber;
import com.yordan.karabelyov.shop.exception.NotFoundException;
import com.yordan.karabelyov.shop.repository.ProductRepository;
import com.yordan.karabelyov.shop.repository.PurchaseRepository;
import com.yordan.karabelyov.shop.repository.SubscriberRepository;
import com.yordan.karabelyov.shop.service.PurchaseService;
import com.yordan.karabelyov.shop.util.ErrorMessages;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    private PurchaseRepository purchaseRepository;
    private ProductRepository productRepository;
    private SubscriberRepository subscriberRepository;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository, ProductRepository productRepository, SubscriberRepository subscriberRepository) {
        this.purchaseRepository = purchaseRepository;
        this.productRepository = productRepository;
        this.subscriberRepository = subscriberRepository;
    }

    @Override
    @Transactional
    public Purchase executePurchase(Long subscriberId, Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessages.PRODUCT_GET_NOT_FOUND, productId)));

        Subscriber subscriber = subscriberRepository.findById(subscriberId)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorMessages.SUBSCRIBER_GET_NOT_FOUND, subscriberId)));

        if (!product.getProductSubscribers().contains(subscriber))
            throw new NotFoundException(String.format(ErrorMessages.SUBSCRIBER_NOT_SUBSCRIBED_TO_PRODUCT, subscriber.getId()));

        Purchase purchase = new Purchase(product, subscriber, quantity);

        return purchaseRepository.save(purchase);
    }
}
