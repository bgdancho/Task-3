package com.yordan.karabelyov.shop.controller;

import com.yordan.karabelyov.shop.dto.subscriber.SubscriberDTO;
import com.yordan.karabelyov.shop.dto.subscriber.SubscriberDetailDTO;
import com.yordan.karabelyov.shop.dto.subscriber.SubscriberReadDTO;
import com.yordan.karabelyov.shop.entity.Subscriber;
import com.yordan.karabelyov.shop.enumeration.SortOrder;
import com.yordan.karabelyov.shop.enumeration.SubscriberOrderBy;
import com.yordan.karabelyov.shop.repository.impl.criteria.holder.SubscriberCriteriaHolder;
import com.yordan.karabelyov.shop.service.SubscriberService;
import com.yordan.karabelyov.shop.util.Constants;
import com.yordan.karabelyov.shop.util.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
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
@RequestMapping(Constants.API_V1 + Constants.SUBSCRIBERS)
public class SubscriberController {
    private final ModelMapper modelMapper;
    private final SubscriberService subscriberService;

    public SubscriberController(ModelMapper modelMapper, SubscriberService subscriberService) {
        this.modelMapper = modelMapper;
        this.subscriberService = subscriberService;
    }

    @Operation(summary = "Create new subscriber.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful subscriber creation."),
            @ApiResponse(responseCode = "400", description = "<p>Bad request with invalid subscriber.</p>",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<SubscriberDetailDTO> createSubscriber(@Valid @RequestBody SubscriberDTO subscriberDTO) {
        Subscriber newSubscriber = modelMapper.map(subscriberDTO, Subscriber.class);
        newSubscriber = subscriberService.create(newSubscriber);
        SubscriberDetailDTO newsubscriberDetailDTO = modelMapper.map(newSubscriber, SubscriberDetailDTO.class);

        return ResponseEntity.ok(newsubscriberDetailDTO);
    }

    @Operation(summary = "Retrieve subscriber by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved subscriber."),
            @ApiResponse(responseCode = "404", description = "Subscriber with given id doesn't exist.", content = @Content)
    })
    @GetMapping("/{subscriberId}")
    public ResponseEntity<SubscriberReadDTO> getSubscriberById(@PathVariable Long subscriberId) {

        SubscriberReadDTO subscribeReadDTO = modelMapper.map(subscriberService.getSubscriberById(subscriberId), SubscriberReadDTO.class);

        return ResponseEntity.ok(subscribeReadDTO);
    }

    @Operation(summary = "Find all subscribers by a given product ID.")
    @GetMapping("/subscribers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved subscribers"),
            @ApiResponse(responseCode = "400", description = "Page number and page size must be greater than 0.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Subscriber with the supplied ID is not found!", content = @Content)
    })
    public ResponseEntity<Page<SubscriberReadDTO>> paginateProductByCriteria(
            @RequestParam @NotEmpty List<Long> productIds,
            @RequestParam(required = false) @Positive Integer pageNumber,
            @RequestParam(required = false) @Positive Integer pageSize,
            @RequestParam(required = false) SortOrder sortOrder,
            @RequestParam(required = false) SubscriberOrderBy orderBy) {

        SubscriberCriteriaHolder holder = new SubscriberCriteriaHolder.Builder()
                .withProductsIds(productIds)
                .withPageNumber(pageNumber)
                .withPageSize(pageSize)
                .withSortOrder(sortOrder)
                .withOrderBy(orderBy)
                .build();

        Page<Subscriber> entityPage = subscriberService.subscriberPagination(holder);

        return ResponseEntity.ok(new Page<>(
                entityPage.getPaginationInformation(),
                entityPage.getContent()
                        .stream()
                        .map(data -> modelMapper.map(data, SubscriberReadDTO.class))
                        .collect(Collectors.toList())));
    }

    @Operation(summary = "Update subscriber.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated subscriber."),
            @ApiResponse(responseCode = "400", description = "<p>Bad request with invalid subscriber.</p>" +
                    "<p>1. Name is too short. Must not be less than: " + Constants.SUBSCRIBER_MIN_NAME_LENGTH + "</p>" +
                    "<p>2. Name is too long. Must not exceed: " + Constants.SUBSCRIBER_MAX_NAME_LENGTH),
            @ApiResponse(responseCode = "404", description = "<p>1. The subscriber you want to update does not exist.</p>"),
            @ApiResponse(responseCode = "409", description = "<p>There is already a subscriber with that email!</p>", content = @Content)
    })
    @PutMapping("/{subscriberId}")
    public ResponseEntity<SubscriberReadDTO> update(@RequestBody @Valid SubscriberDTO subscriberDTO,
                                                    @PathVariable Long subscriberId) {

        Subscriber subscriberToUpdate = modelMapper.map(subscriberDTO, Subscriber.class);

        Subscriber updatedSubscriber = subscriberService.update(subscriberToUpdate, subscriberId);

        return ResponseEntity.ok(modelMapper.map(updatedSubscriber, SubscriberReadDTO.class));
    }

    @Operation(summary = "Delete subscriber.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully deleted subscriber."),
            @ApiResponse(responseCode = "404", description = "Subscriber with given id doesn't exist.", content = @Content)
    })
    @DeleteMapping("/{subscriberId}")
    public ResponseEntity<Void> deleteSubscriber(@PathVariable Long subscriberId) {
        subscriberService.deleteSubscriber(subscriberId);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Add products to a subscriber.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully added products to the subscriber."),
            @ApiResponse(responseCode = "404", description = "Subscriber not found.", content = @Content)
    })
    @PostMapping("/{subscriberId}/products")
    public ResponseEntity<Void> addProducts(
            @PathVariable Long subscriberId,
            @RequestParam List<Long> productIds) {
        subscriberService.addProducts(subscriberId, productIds);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove products from a subscriber.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully removed products from the subscriber."),
            @ApiResponse(responseCode = "404", description = "Subscriber not found.", content = @Content)
    })
    @DeleteMapping("/{subscriberId}/products")
    public ResponseEntity<Void> removeProducts(
            @PathVariable Long subscriberId,
            @RequestParam List<Long> productIds) {
        subscriberService.removeProducts(subscriberId, productIds);
        return ResponseEntity.ok().build();
    }
}
