package com.yordan.karabelyov.shop.service;

import com.yordan.karabelyov.shop.entity.Product;
import com.yordan.karabelyov.shop.entity.ProductStatus;
import com.yordan.karabelyov.shop.entity.Subscriber;
import com.yordan.karabelyov.shop.exception.ConflictException;
import com.yordan.karabelyov.shop.exception.NotFoundException;
import com.yordan.karabelyov.shop.repository.ProductRepository;
import com.yordan.karabelyov.shop.repository.SubscriberRepository;
import com.yordan.karabelyov.shop.repository.impl.criteria.holder.ProductCriteriaHolder;
import com.yordan.karabelyov.shop.service.impl.ProductServiceImpl;
import com.yordan.karabelyov.shop.util.ErrorMessages;
import com.yordan.karabelyov.shop.util.Page;
import com.yordan.karabelyov.shop.util.PaginationInformation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private SubscriberRepository subscriberRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductStatusService productStatusService;

    @Test
    void create_Product_ShouldReturnSavedProduct_WhenValidDataProvided() {
        Product product = new Product();
        product.setName("New Product");

        ProductStatus productStatus = new ProductStatus();
        Long validProductStatusId = 1L;
        productStatus.setId(validProductStatusId);
        product.setProductStatus(productStatus);

        when(productRepository.existsByName(product.getName())).thenReturn(false);
        when(productStatusService.findById(validProductStatusId)).thenReturn(productStatus);
        when(productRepository.save(product)).thenReturn(product);

        Product savedProduct = productService.create(product);

        assertNotNull(savedProduct);
        assertEquals(product.getName(), savedProduct.getName());
        verify(productRepository).save(product);
        verify(productStatusService).findById(validProductStatusId);
    }

    @Test
    void create_Product_ShouldThrowConflictException_WhenDuplicateNameExists() {
        Product product = new Product();
        product.setName("Existing Product");

        when(productRepository.existsByName(product.getName())).thenReturn(true);

        ConflictException exception = assertThrows(ConflictException.class, () -> productService.create(product));

        assertEquals(String.format(ErrorMessages.PRODUCT_GET_DUPLICATE_NAME, product.getName()), exception.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    void getProductById_ShouldReturnProduct_WhenProductExists() {
        Long productId = 1L;
        Product expectedProduct = new Product();
        expectedProduct.setId(productId);
        expectedProduct.setName("Test Product");

        when(productRepository.findProductWithStatusAndSubscribers(productId)).thenReturn(Optional.of(expectedProduct));

        Product actualProduct = productService.getProductById(productId);

        assertNotNull(actualProduct);
        assertEquals(expectedProduct.getId(), actualProduct.getId());
        assertEquals(expectedProduct.getName(), actualProduct.getName());
    }

    @Test
    void getProductById_ShouldThrowNotFoundException_WhenProductDoesNotExist() {
        Long productId = 1L;

        when(productRepository.findProductWithStatusAndSubscribers(productId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> productService.getProductById(productId));

        assertEquals(String.format(ErrorMessages.PRODUCT_GET_NOT_FOUND, productId), exception.getMessage());
    }

    @Test
    void productsPagination_ShouldReturnPaginatedProducts_WhenProductsExist() {
        ProductCriteriaHolder criteriaHolder = new ProductCriteriaHolder.Builder()
                .withSubscribersIds(List.of(1L))
                .withPageNumber(1)
                .withPageSize(10)
                .build();

        List<Product> productList = new ArrayList<>();
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");

        productList.add(product1);
        productList.add(product2);

        PaginationInformation paginationInformation = new PaginationInformation(0, 2, 2);
        Page<Product> expectedPage = new Page<>(paginationInformation, productList);

        when(productRepository.count(criteriaHolder)).thenReturn(2);
        when(productRepository.findAllByCriteria(criteriaHolder)).thenReturn(productList);

        Page<Product> actualPage = productService.productsPagination(criteriaHolder);

        assertNotNull(actualPage);
        assertEquals(expectedPage.getPaginationInformation().getTotalItems(), actualPage.getPaginationInformation().getTotalItems());
    }

    @Test
    void productsPagination_ShouldReturnEmptyPage_WhenNoProductsExist() {
        ProductCriteriaHolder criteriaHolder = new ProductCriteriaHolder.Builder()
                .withSubscribersIds(List.of(1L))
                .withPageNumber(1)
                .withPageSize(10)
                .build();

        when(productRepository.count(criteriaHolder)).thenReturn(0);
        when(productRepository.findAllByCriteria(criteriaHolder)).thenReturn(Collections.emptyList());

        Page<Product> actualPage = productService.productsPagination(criteriaHolder);

        assertNotNull(actualPage);
        assertEquals(0, actualPage.getPaginationInformation().getTotalItems());
    }

    @Test
    void update_Product_ShouldReturnUpdatedProduct_WhenValidDataProvided() {
        Long productId = 1L;
        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Existing Product");

        Product updateProduct = new Product();
        updateProduct.setId(productId);
        updateProduct.setName("Updated Product");

        when(productRepository.findByName("Updated Product")).thenReturn(null);
        when(productRepository.save(updateProduct)).thenReturn(updateProduct);

        Product actualProduct = productService.update(updateProduct, productId);

        assertNotNull(actualProduct);
        assertEquals(updateProduct.getName(), actualProduct.getName());
        assertEquals(productId, actualProduct.getId());
    }

    @Test
    void update_Product_ShouldThrowConflictException_WhenProductNameAlreadyExists() {
        Long productId = 1L;
        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Existing Product");

        Product updateProduct = new Product();
        updateProduct.setName("Existing Product");

        when(productRepository.findByName("Existing Product")).thenReturn(existingProduct);

        ConflictException exception = assertThrows(ConflictException.class, () -> {
            productService.update(updateProduct, 2L);
        });

        assertEquals(String.format(ErrorMessages.PRODUCT_GET_DUPLICATE_NAME, "Existing Product"), exception.getMessage());
    }

    @Test
    void deleteProduct_ShouldDeleteProduct_WhenProductExists() {
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);

        when(productRepository.findProductWithStatusAndSubscribers(productId)).thenReturn(Optional.of(product));

        Subscriber subscriber = new Subscriber();
        subscriber.setProducts(new HashSet<>(List.of(product)));
        product.getProductSubscribers().add(subscriber);

        productService.deleteProduct(productId);

        verify(productRepository).findProductWithStatusAndSubscribers(productId);
        verify(productRepository).delete(product);
    }

    @Test
    void deleteProduct_ShouldThrowNotFoundException_WhenProductDoesNotExist() {
        Long productId = 1L;

        when(productRepository.findProductWithStatusAndSubscribers(productId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            productService.deleteProduct(productId);
        });

        assertEquals(String.format(ErrorMessages.PRODUCT_GET_NOT_FOUND, productId), exception.getMessage());
    }

    @Test
    void addSubscribers_ShouldAddSubscribers_WhenProductAndSubscribersExist() {
        Long productId = 1L;
        List<Long> subscriberIds = List.of(1L, 2L);

        Product product = new Product();
        product.setId(productId);
        product.setProductSubscribers(new HashSet<>());

        when(productRepository.findProductWithStatusAndSubscribers(productId)).thenReturn(Optional.of(product));

        Subscriber subscriber1 = new Subscriber();
        subscriber1.setId(1L);
        subscriber1.setProducts(new HashSet<>());

        Subscriber subscriber2 = new Subscriber();
        subscriber2.setId(2L);
        subscriber2.setProducts(new HashSet<>());

        when(subscriberRepository.findAllById(subscriberIds)).thenReturn(List.of(subscriber1, subscriber2));

        productService.addSubscribers(productId, subscriberIds);

        verify(productRepository).findProductWithStatusAndSubscribers(productId);
        verify(subscriberRepository).findAllById(subscriberIds);
        assertEquals(2, product.getProductSubscribers().size());
        assertTrue(product.getProductSubscribers().contains(subscriber1));
        assertTrue(product.getProductSubscribers().contains(subscriber2));
    }

    @Test
    void addSubscribers_ShouldThrowNotFoundException_WhenProductDoesNotExist() {
        Long productId = 1L;
        List<Long> subscriberIds = List.of(1L, 2L);

        when(productRepository.findProductWithStatusAndSubscribers(productId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            productService.addSubscribers(productId, subscriberIds);
        });

        assertEquals(String.format(ErrorMessages.PRODUCT_GET_NOT_FOUND, productId), exception.getMessage());
    }

    @Test
    void removeSubscribers_ShouldRemoveSubscribers_WhenProductAndSubscribersExist() {
        Long productId = 1L;
        List<Long> subscriberIds = List.of(1L, 2L);

        Product product = new Product();
        product.setId(productId);
        product.setProductSubscribers(new HashSet<>());

        Subscriber subscriber1 = new Subscriber();
        subscriber1.setId(1L);
        subscriber1.setProducts(new HashSet<>());

        Subscriber subscriber2 = new Subscriber();
        subscriber2.setId(2L);
        subscriber2.setProducts(new HashSet<>());

        product.getProductSubscribers().add(subscriber1);
        product.getProductSubscribers().add(subscriber2);

        when(productRepository.findProductWithStatusAndSubscribers(productId)).thenReturn(Optional.of(product));

        when(subscriberRepository.findAllById(subscriberIds)).thenReturn(List.of(subscriber1, subscriber2));

        productService.removeSubscribers(productId, subscriberIds);

        verify(productRepository).findProductWithStatusAndSubscribers(productId);
        verify(subscriberRepository).findAllById(subscriberIds);
        assertEquals(0, product.getProductSubscribers().size());
    }

    @Test
    void removeSubscribers_ShouldThrowNotFoundException_WhenProductDoesNotExist() {
        Long productId = 1L;
        List<Long> subscriberIds = List.of(1L, 2L);

        when(productRepository.findProductWithStatusAndSubscribers(productId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            productService.removeSubscribers(productId, subscriberIds);
        });

        assertEquals(String.format(ErrorMessages.PRODUCT_GET_NOT_FOUND, productId), exception.getMessage());
    }
}
