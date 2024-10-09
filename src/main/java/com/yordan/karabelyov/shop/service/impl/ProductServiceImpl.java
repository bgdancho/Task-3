package com.yordan.karabelyov.shop.service.impl;

import com.yordan.karabelyov.shop.entity.Product;
import com.yordan.karabelyov.shop.entity.ProductStatus;
import com.yordan.karabelyov.shop.entity.Subscriber;
import com.yordan.karabelyov.shop.exception.ConflictException;
import com.yordan.karabelyov.shop.exception.NotFoundException;
import com.yordan.karabelyov.shop.repository.ProductRepository;
import com.yordan.karabelyov.shop.repository.SubscriberRepository;
import com.yordan.karabelyov.shop.repository.impl.criteria.holder.ProductCriteriaHolder;
import com.yordan.karabelyov.shop.service.ProductService;
import com.yordan.karabelyov.shop.service.ProductStatusService;
import com.yordan.karabelyov.shop.util.ErrorMessages;
import com.yordan.karabelyov.shop.util.PaginationInformation;
import com.yordan.karabelyov.shop.util.PaginationUtil;
import com.yordan.karabelyov.shop.util.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;

    SubscriberRepository subscriberRepository;

    ProductStatusService productStatusService;

    public ProductServiceImpl(ProductRepository productRepository, SubscriberRepository subscriberRepository, ProductStatusService productStatusService) {
        this.productRepository = productRepository;
        this.subscriberRepository = subscriberRepository;
        this.productStatusService = productStatusService;
    }

    @Override
    public Product create(Product product) {
        if (productRepository.existsByName(product.getName())){
            throw new ConflictException(String.format(ErrorMessages.PRODUCT_GET_DUPLICATE_NAME, product.getName()));
        }
        ProductStatus productStatus = productStatusService.findById(product.getProductStatus().getId());
        product.setProductStatus(productStatus);

        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Long productId) {
        Optional<Product> product = productRepository.findProductWithStatusAndSubscribers(productId);

        if (product.isEmpty()) {
            throw new NotFoundException(String.format(ErrorMessages.PRODUCT_GET_NOT_FOUND, productId));
        }
        return product.get();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> productsPagination(ProductCriteriaHolder holder) {

        PaginationInformation paginationInformation = PaginationUtil.constructPaginationHolder(
                holder.getPageNumber(),
                holder.getPageSize(),
                productRepository.count(holder)
        );

        List<Product> fetchedProducts = productRepository.findAllByCriteria(holder);

        return new Page<>(paginationInformation, fetchedProducts);
    }

    @Override
    public Product update(Product updateProduct, Long id) {
        String updateProductName = updateProduct.getName();
        Product existingProduct = productRepository.findByName(updateProductName);

        if (existingProduct != null && !existingProduct.getId().equals(id)) {
            throw new ConflictException(String.format(ErrorMessages.PRODUCT_GET_DUPLICATE_NAME, updateProductName));
        }

        return productRepository.save(updateProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        Product product = getProductById(productId);

        product.getProductSubscribers().forEach(subscriber -> subscriber.getProducts().remove(product));

        productRepository.delete(product);
    }

    @Override
    public void addSubscribers(Long productId, List<Long> subscriberIds) {
        Product product = getProductById(productId);

        List<Subscriber> subscribers = subscriberRepository.findAllById(subscriberIds);

        product.getProductSubscribers().addAll(subscribers);

        for (Subscriber subscriber : subscribers) {
            subscriber.getProducts().add(product);
        }
        productRepository.save(product);
    }

    @Override
    public void removeSubscribers(Long productId, List<Long> subscriberIds) {
        Product product = getProductById(productId);

        List<Subscriber> subscribers = subscriberRepository.findAllById(subscriberIds);

        subscribers.forEach(product.getProductSubscribers()::remove);

        for (Subscriber subscriber : subscribers) {
            subscriber.getProducts().remove(product);
        }

        productRepository.save(product);
    }
}

