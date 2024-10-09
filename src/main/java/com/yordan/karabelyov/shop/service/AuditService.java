package com.yordan.karabelyov.shop.service;

import com.yordan.karabelyov.shop.entity.Product;
import com.yordan.karabelyov.shop.entity.ProductStatus;

import java.time.LocalDate;
import java.util.List;

public interface AuditService {
    long getSubscribersCount();
    Long countSoldProducts(LocalDate startDate, LocalDate endDate, ProductStatus productStatus);
    List<Product> getMostPopularProducts(int limit);
}
