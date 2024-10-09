package com.yordan.karabelyov.shop.controller;

import com.yordan.karabelyov.shop.entity.Purchase;
import com.yordan.karabelyov.shop.service.PurchaseService;
import com.yordan.karabelyov.shop.util.Constants;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.API_V1 + Constants.PURCHASES)
public class PurchaseController {
    private PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping
    public ResponseEntity<Purchase> processPurchase(
            @RequestParam @NotNull Long subscriberId,
            @RequestParam @NotNull Long productId,
            @RequestParam @NotNull int quantity) {
        Purchase purchase = purchaseService.executePurchase(subscriberId, productId, quantity);
        return new ResponseEntity<>(purchase, HttpStatus.ACCEPTED);
    }
}
