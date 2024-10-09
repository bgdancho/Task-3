package com.yordan.karabelyov.shop.controller;

import com.yordan.karabelyov.shop.dto.product.ProductCreateDTO;
import com.yordan.karabelyov.shop.dto.product.ProductDTO;
import com.yordan.karabelyov.shop.entity.Product;
import com.yordan.karabelyov.shop.enumeration.ProductOrderBy;
import com.yordan.karabelyov.shop.enumeration.SortOrder;
import com.yordan.karabelyov.shop.repository.impl.criteria.holder.ProductCriteriaHolder;
import com.yordan.karabelyov.shop.service.ProductService;
import com.yordan.karabelyov.shop.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import com.yordan.karabelyov.shop.util.Page;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping(Constants.API_V1 + Constants.PRODUCTS)
public class ProductController {
    private final ModelMapper modelMapper;

    private final ProductService productService;

    public ProductController(ModelMapper modelMapper, ProductService productService) {
        this.modelMapper = modelMapper;
        this.productService = productService;
    }

    @Operation(summary = "Create new product.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful product creation."),
            @ApiResponse(responseCode = "400", description = "<p>Bad request with invalid product.</p>",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductCreateDTO productCreateDTO) {
        Product newProduct = modelMapper.map(productCreateDTO, Product.class);
        newProduct = productService.create(newProduct);
        ProductDTO newProductDTO = modelMapper.map(newProduct, ProductDTO.class);

        return ResponseEntity.ok(newProductDTO);
    }

    @Operation(summary = "Retrieve product by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved product."),
            @ApiResponse(responseCode = "404", description = "Product with given id doesn't exist.", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {

        ProductDTO productDTO = modelMapper.map(productService.getProductById(id), ProductDTO.class);

        return ResponseEntity.ok(productDTO);
    }

    @Operation(summary = "Find all products by a given subscriber ID.")
    @GetMapping("/products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products"),
            @ApiResponse(responseCode = "400", description = "Page number and page size must be greater than 0.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Subscriber with the supplied ID is not found!", content = @Content)
    })
    public ResponseEntity<Page<ProductDTO>> paginateProductByCriteria(
            @RequestParam @NotEmpty List<Long> subscriberIds,
            @RequestParam(required = false) @Positive Integer pageNumber,
            @RequestParam(required = false) @Positive Integer pageSize,
            @RequestParam(required = false) SortOrder sortOrder,
            @RequestParam(required = false) ProductOrderBy orderBy) {

        ProductCriteriaHolder holder = new ProductCriteriaHolder.Builder()
                .withSubscribersIds(subscriberIds)
                .withPageNumber(pageNumber)
                .withPageSize(pageSize)
                .withSortOrder(sortOrder)
                .withOrderBy(orderBy)
                .build();

        Page<Product> entityPage = productService.productsPagination(holder);

        return ResponseEntity.ok(new Page<>(
                entityPage.getPaginationInformation(),
                entityPage.getContent()
                        .stream()
                        .map(data -> modelMapper.map(data, ProductDTO.class))
                        .collect(Collectors.toList())));
    }

    @Operation(summary = "Update product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated product."),
            @ApiResponse(responseCode = "400", description = "<p>Bad request with invalid product.</p>" +
                    "<p>1. Name is too short. Must not be less than: " + Constants.PRODUCT_MIN_NAME_LENGTH + "</p>" +
                    "<p>2. Name is too long. Must not exceed: " + Constants.PRODUCT_MAX_NAME_LENGTH + "</p>" +
                    "<p>3. Description is too long. Must not exceed: " + Constants.TEXT_LENGTH + "</p>"),
            @ApiResponse(responseCode = "404", description = "<p>1. The product you want to update does not exist.</p>"),
            @ApiResponse(responseCode = "409", description = "<p>There is already a product with that name!</p>", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@RequestBody @Valid ProductDTO productUpdateDTO,
                                             @PathVariable Long id) {

        Product productToUpdate = modelMapper.map(productUpdateDTO, Product.class);

        Product updatedProduct = productService.update(productToUpdate, id);

        return ResponseEntity.ok(modelMapper.map(updatedProduct, ProductDTO.class));
    }

    @Operation(summary = "Delete product.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully deleted product."),
            @ApiResponse(responseCode = "404", description = "Product with given id doesn't exist or the product you're trying to delete is not in subscriptions.", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Add subscribers to a product.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully added subscribers to the product."),
            @ApiResponse(responseCode = "404", description = "Product not found.", content = @Content)
    })
    @PostMapping("/{productId}/subscribers")
    public ResponseEntity<Void> addSubscriber(
            @PathVariable Long productId,
            @RequestParam List<Long> subscriberIds) {
        productService.addSubscribers(productId, subscriberIds);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove subscribers from a product.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully removed subscribers from the product."),
            @ApiResponse(responseCode = "404", description = "Product not found.", content = @Content)
    })
    @DeleteMapping("/{productId}/subscribers")
    public ResponseEntity<Void> removeSubscriber(
            @PathVariable Long productId,
            @RequestParam List<Long> subscriberIds) {
        productService.removeSubscribers(productId, subscriberIds);
        return ResponseEntity.ok().build();
    }
}
