package com.yordan.karabelyov.shop.service.impl;

import com.yordan.karabelyov.shop.entity.Product;
import com.yordan.karabelyov.shop.entity.ProductStatus;
import com.yordan.karabelyov.shop.repository.PurchaseRepository;
import com.yordan.karabelyov.shop.repository.SubscriberRepository;
import com.yordan.karabelyov.shop.service.AuditService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AuditServiceImpl implements AuditService {
    SubscriberRepository subscriberRepository;
    PurchaseRepository purchaseRepository;

    public AuditServiceImpl(SubscriberRepository subscriberRepository, PurchaseRepository purchaseRepository) {
        this.subscriberRepository = subscriberRepository;
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public long getSubscribersCount() {
        return subscriberRepository.count();
    }

    @Override
    public Long countSoldProducts(LocalDate startDate, LocalDate endDate, ProductStatus productStatus) {
        Long productsSold = purchaseRepository.countSoldProducts(startDate, endDate, productStatus.getId());
        return productsSold != null ? productsSold : 0L;
    }
    @Override
    public List<Product> getMostPopularProducts(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return purchaseRepository.findMostPopularProducts(pageable);
    }
}



