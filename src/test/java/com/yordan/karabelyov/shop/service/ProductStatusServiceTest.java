package com.yordan.karabelyov.shop.service;

import com.yordan.karabelyov.shop.entity.ProductStatus;
import com.yordan.karabelyov.shop.enumeration.ProductStatusEnum;
import com.yordan.karabelyov.shop.exception.NotFoundException;
import com.yordan.karabelyov.shop.repository.ProductStatusRepository;
import com.yordan.karabelyov.shop.service.impl.ProductStatusServiceImpl;
import com.yordan.karabelyov.shop.util.ErrorMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductStatusServiceTest {

    @Mock
    private ProductStatusRepository productStatusRepository;

    @InjectMocks
    private ProductStatusServiceImpl productStatusService;

    private ProductStatus availableStatus;
    private ProductStatus outOfStockStatus;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        availableStatus = new ProductStatus(ProductStatusEnum.IN_STOCK);
        availableStatus.setId(1L);
        outOfStockStatus = new ProductStatus(ProductStatusEnum.AWAITING_DELIVERY);
        outOfStockStatus.setId(2L);

        when(productStatusRepository.findAll()).thenReturn(Arrays.asList(availableStatus, outOfStockStatus));

        productStatusService = new ProductStatusServiceImpl(productStatusRepository);
    }

    @Test
    void testFindById_ReturnsProductStatus_WhenExists() {
        Long statusId = 1L;

        ProductStatus result = productStatusService.findById(statusId);

        assertNotNull(result);
        assertEquals(availableStatus.getId(), result.getId());
        assertEquals(availableStatus.getName(), result.getName());
    }

    @Test
    void testFindById_ThrowsNotFoundException_WhenDoesNotExist() {
        Long nonExistentId = 3L;

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> productStatusService.findById(nonExistentId));
        assertEquals(ErrorMessages.PRODUCT_STATUS_NOT_FOUND + nonExistentId, thrown.getMessage());
    }

    @Test
    void testFindById_ReturnsOutOfStockStatus_WhenExists() {
        Long statusId = 2L;

        ProductStatus result = productStatusService.findById(statusId);

        assertNotNull(result);
        assertEquals(outOfStockStatus.getId(), result.getId());
        assertEquals(outOfStockStatus.getName(), result.getName());
    }
}
