package com.yordan.karabelyov.shop.controller;

import com.yordan.karabelyov.shop.dto.misc.TotalAmountDTO;
import com.yordan.karabelyov.shop.dto.product.ProductDTO;
import com.yordan.karabelyov.shop.entity.ProductStatus;
import com.yordan.karabelyov.shop.service.AuditService;
import com.yordan.karabelyov.shop.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(Constants.API_V1 + Constants.AUDIT)
public class AuditController {
    private final AuditService auditService;
    private final ModelMapper modelMapper;

    public AuditController(AuditService auditService, ModelMapper modelMapper) {
        this.auditService = auditService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Finds the total number of subscribers.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the total number of subscribers"),
    })
    @GetMapping("/subscribers/count")
    public ResponseEntity<TotalAmountDTO> getNumberOfSubscribers() {
        long totalSubscribersCount = auditService.getSubscribersCount();
        return ResponseEntity.ok(new TotalAmountDTO(totalSubscribersCount));
    }

    @Operation(summary = "Finds the total number of sold product for period of time.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the total number of sold products"),
            @ApiResponse(responseCode = "400", description = "Required \"productStatus\" parameter is not present.", content = @Content),
    })
    @GetMapping("/products/sold")
    public ResponseEntity<TotalAmountDTO> countSoldProducts(
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate,
            @RequestParam(value = "productStatus") @NotNull ProductStatus productStatus) {
        final Long totalSoldProducts = auditService.countSoldProducts(startDate, endDate, productStatus);

        return ResponseEntity.ok(new TotalAmountDTO(totalSoldProducts));
    }

    @GetMapping("/products/most-popular")
    public ResponseEntity<List<ProductDTO>> getMostPopularProducts(@RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<ProductDTO> mostPopularProducts = auditService.getMostPopularProducts(limit)
                .stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        return ResponseEntity.ok(mostPopularProducts);

    }
}
