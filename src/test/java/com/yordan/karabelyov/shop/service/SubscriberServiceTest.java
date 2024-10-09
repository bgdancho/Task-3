package com.yordan.karabelyov.shop.service;

import com.yordan.karabelyov.shop.entity.Product;
import com.yordan.karabelyov.shop.entity.Subscriber;
import com.yordan.karabelyov.shop.exception.NotFoundException;
import com.yordan.karabelyov.shop.repository.ProductRepository;
import com.yordan.karabelyov.shop.repository.SubscriberRepository;
import com.yordan.karabelyov.shop.repository.impl.criteria.holder.SubscriberCriteriaHolder;
import com.yordan.karabelyov.shop.service.impl.SubscriberServiceImpl;
import com.yordan.karabelyov.shop.util.ErrorMessages;
import com.yordan.karabelyov.shop.util.Page;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubscriberServiceTest {
    @Mock
    SubscriberRepository subscriberRepository;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    SubscriberServiceImpl subscriberService;


    @Test
    void validateSubscriberExistence_ShouldThrowNotFoundException_WhenSubscriberDoesNotExist() {
        Long subscriberId = 1L;

        when(subscriberRepository.existsById(subscriberId)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            subscriberService.validateSubscriberExistence(subscriberId);
        });

        assertEquals(String.format(ErrorMessages.SUBSCRIBER_GET_NOT_FOUND, subscriberId), exception.getMessage());
    }

    @Test
    void validateSubscriberExistence_ShouldNotThrow_WhenSubscriberExists() {
        Long subscriberId = 1L;

        when(subscriberRepository.existsById(subscriberId)).thenReturn(true);

        assertDoesNotThrow(() -> subscriberService.validateSubscriberExistence(subscriberId));
    }

    @Test
    void create_ShouldReturnSavedSubscriber_WhenSubscriberIsCreated() {
        Subscriber subscriber = new Subscriber();

        when(subscriberRepository.save(subscriber)).thenReturn(subscriber);

        Subscriber createdSubscriber = subscriberService.create(subscriber);

        assertNotNull(createdSubscriber);
        assertEquals(subscriber, createdSubscriber);
    }

    @Test
    void getSubscriberById_ShouldThrowNotFoundException_WhenSubscriberDoesNotExist() {
        Long subscriberId = 1L;

        when(subscriberRepository.findById(subscriberId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            subscriberService.getSubscriberById(subscriberId);
        });

        assertEquals(String.format(ErrorMessages.SUBSCRIBER_GET_NOT_FOUND, subscriberId), exception.getMessage());
    }

    @Test
    void getSubscriberById_ShouldReturnSubscriber_WhenSubscriberExists() {
        Long subscriberId = 1L;
        Subscriber subscriber = new Subscriber();

        when(subscriberRepository.findById(subscriberId)).thenReturn(Optional.of(subscriber));

        Subscriber foundSubscriber = subscriberService.getSubscriberById(subscriberId);

        assertNotNull(foundSubscriber);
        assertEquals(subscriber, foundSubscriber);
    }

    @Test
    void subscriberPagination_ShouldReturnPageOfSubscribers_WhenValidRequest() {
        SubscriberCriteriaHolder holder = new SubscriberCriteriaHolder.Builder()
                .withPageNumber(0)
                .withPageSize(10)
                .build();

        List<Subscriber> subscribers = List.of(new Subscriber(), new Subscriber());
        when(subscriberRepository.count(holder)).thenReturn(subscribers.size());
        when(subscriberRepository.findAllByCriteria(holder)).thenReturn(subscribers);

        Page<Subscriber> page = subscriberService.subscriberPagination(holder);

        assertNotNull(page);
        assertEquals(subscribers.size(), page.getContent().size());
    }

    @Test
    void update_ShouldThrowNotFoundException_WhenSubscriberDoesNotExist() {
        Long subscriberId = 1L;
        Subscriber updateSubscriber = new Subscriber();

        when(subscriberRepository.existsById(subscriberId)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            subscriberService.update(updateSubscriber, subscriberId);
        });

        assertEquals(String.format(ErrorMessages.SUBSCRIBER_GET_NOT_FOUND, subscriberId), exception.getMessage());
    }

    @Test
    void update_ShouldReturnUpdatedSubscriber_WhenSubscriberExists() {
        Long subscriberId = 1L;
        Subscriber updateSubscriber = new Subscriber();
        Subscriber existingSubscriber = new Subscriber();

        when(subscriberRepository.existsById(subscriberId)).thenReturn(true);
        when(subscriberRepository.findById(subscriberId)).thenReturn(Optional.of(existingSubscriber));
        when(subscriberRepository.save(existingSubscriber)).thenReturn(existingSubscriber);

        Subscriber updatedSubscriber = subscriberService.update(updateSubscriber, subscriberId);

        assertNotNull(updatedSubscriber);
        assertEquals(existingSubscriber, updatedSubscriber);
    }

    @Test
    void deleteSubscriber_ShouldThrowNotFoundException_WhenSubscriberDoesNotExist() {
        Long subscriberId = 1L;

        when(subscriberRepository.findById(subscriberId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            subscriberService.deleteSubscriber(subscriberId);
        });

        assertEquals(String.format(ErrorMessages.SUBSCRIBER_GET_NOT_FOUND, subscriberId), exception.getMessage());
    }

    @Test
    void deleteSubscriber_ShouldDeleteSubscriber_WhenSubscriberExists() {
        Long subscriberId = 1L;
        Subscriber subscriber = new Subscriber();

        when(subscriberRepository.findById(subscriberId)).thenReturn(Optional.of(subscriber));

        assertDoesNotThrow(() -> subscriberService.deleteSubscriber(subscriberId));
        verify(subscriberRepository).delete(subscriber);
    }

    @Test
    void addProducts_ShouldThrowNotFoundException_WhenSubscriberDoesNotExist() {
        Long subscriberId = 1L;
        List<Long> productIds = List.of(1L, 2L);

        when(subscriberRepository.findById(subscriberId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            subscriberService.addProducts(subscriberId, productIds);
        });

        assertEquals(String.format(ErrorMessages.SUBSCRIBER_GET_NOT_FOUND, subscriberId), exception.getMessage());
    }

    @Test
    void addProducts_ShouldAddProductsToSubscriber_WhenSubscriberExists() {
        Long subscriberId = 1L;
        List<Long> productIds = List.of(1L, 2L);
        Subscriber subscriber = new Subscriber();

        Product product1 = new Product();
        product1.setId(1L);
        Product product2 = new Product();
        product2.setId(2L);

        when(subscriberRepository.findById(subscriberId)).thenReturn(Optional.of(subscriber));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product2));

        subscriberService.addProducts(subscriberId, productIds);

        assertEquals(2, subscriber.getProducts().size());
        assertTrue(subscriber.getProducts().contains(product1));
        assertTrue(subscriber.getProducts().contains(product2));
    }

    @Test
    void removeProducts_ShouldThrowNotFoundException_WhenSubscriberDoesNotExist() {
        Long subscriberId = 1L;
        List<Long> productIds = List.of(1L, 2L);

        when(subscriberRepository.findById(subscriberId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            subscriberService.removeProducts(subscriberId, productIds);
        });

        assertEquals(String.format(ErrorMessages.SUBSCRIBER_GET_NOT_FOUND, subscriberId), exception.getMessage());
    }

    @Test
    void removeProducts_ShouldRemoveProducts_WhenProductsExist() {
        Long subscriberId = 1L;
        List<Long> productIds = List.of(1L, 2L);

        Subscriber subscriber = new Subscriber();

        Product product1 = new Product();
        product1.setId(1L);
        Product product2 = new Product();
        product2.setId(2L);

        subscriber.getProducts().add(product1);
        subscriber.getProducts().add(product2);

        when(subscriberRepository.findById(subscriberId)).thenReturn(Optional.of(subscriber));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product2));

        subscriberService.removeProducts(subscriberId, productIds);

        assertFalse(subscriber.getProducts().contains(product1));
        assertFalse(subscriber.getProducts().contains(product2));
    }

    @Test
    void removeProducts_ShouldThrowNotFoundException_WhenProductDoesNotExist() {
        Long subscriberId = 1L;
        List<Long> productIds = List.of(1L, 2L);

        Subscriber subscriber = new Subscriber();

        Product product1 = new Product();
        product1.setId(1L);

        subscriber.getProducts().add(product1);

        when(subscriberRepository.findById(subscriberId)).thenReturn(Optional.of(subscriber));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            subscriberService.removeProducts(subscriberId, productIds);
        });

        assertEquals(ErrorMessages.PRODUCT_NOT_FOUND, exception.getMessage());
    }
}
