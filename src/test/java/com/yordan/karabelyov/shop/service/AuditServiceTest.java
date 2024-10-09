package com.yordan.karabelyov.shop.service;

import com.yordan.karabelyov.shop.entity.Product;
import com.yordan.karabelyov.shop.entity.ProductStatus;
import com.yordan.karabelyov.shop.repository.PurchaseRepository;
import com.yordan.karabelyov.shop.repository.SubscriberRepository;
import com.yordan.karabelyov.shop.service.impl.AuditServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuditServiceTest {
    @Mock
    private SubscriberRepository subscriberRepository;

    @Mock
    private PurchaseRepository purchaseRepository;

    @InjectMocks
    private AuditServiceImpl auditService;

    @BeforeEach
    public void setUp() {
        auditService = new AuditServiceImpl(subscriberRepository, purchaseRepository);
    }

    @Test
    public void testGetSubscribersCount() {
        long expectedCount = 5L;
        when(subscriberRepository.count()).thenReturn(expectedCount);

        long actualCount = auditService.getSubscribersCount();

        assertEquals(expectedCount, actualCount);
        verify(subscriberRepository, times(1)).count();
    }

    @Test
    public void testCountSoldProducts() {
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now();
        ProductStatus productStatus = mock(ProductStatus.class);

        when(productStatus.getId()).thenReturn(1L);
        when(purchaseRepository.countSoldProducts(startDate, endDate, productStatus.getId())).thenReturn(10L);

        Long result = auditService.countSoldProducts(startDate, endDate, productStatus);

        assertEquals(10L, result);
        verify(purchaseRepository, times(1)).countSoldProducts(startDate, endDate, productStatus.getId());
    }

    @Test
    public void testCountSoldProductsReturnsZeroWhenNoProductsSold() {
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now();
        ProductStatus productStatus = mock(ProductStatus.class);

        when(productStatus.getId()).thenReturn(1L);
        when(purchaseRepository.countSoldProducts(startDate, endDate, productStatus.getId())).thenReturn(null);

        Long result = auditService.countSoldProducts(startDate, endDate, productStatus);

        assertEquals(0L, result);
        verify(purchaseRepository, times(1)).countSoldProducts(startDate, endDate, productStatus.getId());
    }

    @Test
    public void testGetMostPopularProducts() {
        int limit = 5;
        Product product1 = new Product();
        Product product2 = new Product();
        List<Product> expectedProducts = Arrays.asList(product1, product2);

        when(purchaseRepository.findMostPopularProducts(any())).thenReturn(expectedProducts);

        List<Product> actualProducts = auditService.getMostPopularProducts(limit);

        assertEquals(expectedProducts, actualProducts);
        verify(purchaseRepository, times(1)).findMostPopularProducts(any());
    }
}

