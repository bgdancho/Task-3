package com.yordan.karabelyov.shop.service.impl;

import com.yordan.karabelyov.shop.entity.ProductStatus;
import com.yordan.karabelyov.shop.exception.NotFoundException;
import com.yordan.karabelyov.shop.repository.ProductStatusRepository;
import com.yordan.karabelyov.shop.service.ProductStatusService;
import com.yordan.karabelyov.shop.util.ErrorMessages;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

@Service
public class ProductStatusServiceImpl implements ProductStatusService {

    private final List<ProductStatus> productStatusCache;
    private final Map<Long, ProductStatus> productStatusById;
    private final ProductStatusRepository productStatusRepository;

    public ProductStatusServiceImpl(ProductStatusRepository productStatusRepository) {
        this.productStatusRepository = productStatusRepository;

        productStatusCache = List.copyOf(productStatusRepository.findAll());
        productStatusById = productStatusCache.stream()
                .collect(toUnmodifiableMap(ProductStatus::getId, Function.identity()));
    }

    @Override
    public ProductStatus findById(Long id) {
        ProductStatus productStatus = productStatusById.get(id);
        if (productStatus == null) {
            throw new NotFoundException(ErrorMessages.PRODUCT_STATUS_NOT_FOUND + id);
        }
        return productStatus;
    }
}
