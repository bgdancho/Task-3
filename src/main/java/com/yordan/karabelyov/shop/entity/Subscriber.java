package com.yordan.karabelyov.shop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.yordan.karabelyov.shop.util.Constants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@NamedEntityGraph(
        name = Subscriber.ATTR_PRODUCTS,
        attributeNodes = @NamedAttributeNode(Subscriber.ATTR_PRODUCTS)
)

@Entity
@Table(name="subscribers")
public class Subscriber extends BaseAuditEntity {

    public static final String ATTR_PRODUCTS = "products";

    @NotNull
    @Size(min = Constants.SUBSCRIBER_MIN_NAME_LENGTH, max = Constants.SUBSCRIBER_MAX_NAME_LENGTH)
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Size(min = Constants.SUBSCRIBER_MIN_NAME_LENGTH, max = Constants.SUBSCRIBER_MAX_NAME_LENGTH)
    @Column(name = "last_name")
    private String lastName;

    @ManyToMany(mappedBy = "productSubscribers")
    @JsonBackReference
    private Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "subscriber")
    private List<Purchase> purchases = new ArrayList<>();

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subscriber that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(products, that.products) && Objects.equals(purchases, that.purchases);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstName, lastName);
    }
}
