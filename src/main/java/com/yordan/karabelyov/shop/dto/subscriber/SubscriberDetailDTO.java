package com.yordan.karabelyov.shop.dto.subscriber;

import com.yordan.karabelyov.shop.entity.Product;
import com.yordan.karabelyov.shop.util.Constants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public class SubscriberDetailDTO {

    @NotNull
    @Size(min = Constants.SUBSCRIBER_MIN_NAME_LENGTH, max = Constants.SUBSCRIBER_MAX_NAME_LENGTH)
    private String firstName;

    @NotNull
    @Size(min = Constants.SUBSCRIBER_MIN_NAME_LENGTH, max = Constants.SUBSCRIBER_MAX_NAME_LENGTH)
    private String lastName;

    private LocalDateTime createdAt;

    private List<Product> products;

    public SubscriberDetailDTO() {
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
