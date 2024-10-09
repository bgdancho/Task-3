package com.yordan.karabelyov.shop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "purchases")

public class Purchase extends BaseAuditEntity {
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "subscriber_id", nullable = false)
    private Subscriber subscriber;

    private Integer quantity;

    public Purchase() {
    }

    public Purchase( Product product, Subscriber subscriber, Integer quantity) {
        this.product = product;
        this.subscriber = subscriber;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Purchase purchase)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(product, purchase.product) && Objects.equals(subscriber, purchase.subscriber) && Objects.equals(quantity, purchase.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), product, subscriber, quantity);
    }
}
