package com.yordan.karabelyov.shop.service;

import com.yordan.karabelyov.shop.entity.Purchase;

public interface PurchaseService {
    Purchase executePurchase(Long subscriberId, Long productId, int quantity);
}
