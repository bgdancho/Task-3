package com.yordan.karabelyov.shop.service;

import com.yordan.karabelyov.shop.entity.Product;
import com.yordan.karabelyov.shop.repository.impl.criteria.holder.ProductCriteriaHolder;
import com.yordan.karabelyov.shop.util.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    Product create(Product product);

    Product getProductById(Long id);

    Page<Product> productsPagination(ProductCriteriaHolder params);

    Product update(Product product, Long id);

    void deleteProduct(Long id);

    void addSubscribers(Long productId, List<Long> subscriberIds);

    void removeSubscribers(Long productId, List<Long> subscriberIds);
}
