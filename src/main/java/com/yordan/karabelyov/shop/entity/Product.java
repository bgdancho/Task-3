package com.yordan.karabelyov.shop.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.yordan.karabelyov.shop.util.Constants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@NamedEntityGraph(
        name = Product.ATTR_SUBSCRIBERS,
        attributeNodes = @NamedAttributeNode(Product.ATTR_SUBSCRIBERS)
)

@Entity
@Table(name = "products")
public class Product extends BaseAuditEntity {
    public static final String ATTR_SUBSCRIBERS = "productSubscribers";
    public static final String ATTR_STATUS = "productStatus";

    @NotNull
    @Size(min = Constants.PRODUCT_MIN_NAME_LENGTH, max = Constants.PRODUCT_MAX_NAME_LENGTH)
    @Column(name = "name")
    private String name;

    @NotNull
    @Size(max = Constants.TEXT_LENGTH)
    @Column(name = "description")
    private String description;

    @NotNull
    @Min(value = Constants.MIN_PRICE)
    @Column(name = "price")
    private Integer price;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_subscribers",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "subscriber_id"))
    @JsonManagedReference
    private Set<Subscriber> productSubscribers = new HashSet<>();

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private ProductStatus productStatus;

    @OneToMany(mappedBy = "product")
    private List<Purchase> purchases = new ArrayList<>();

    public Product() {
    }

    public Product(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Set<Subscriber> getProductSubscribers() {
        return productSubscribers;
    }

    public void setProductSubscribers(Set<Subscriber> productSubscribers) {
        this.productSubscribers = productSubscribers;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
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
        if (!(o instanceof Product product)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(name, product.name) && Objects.equals(description, product.description) && Objects.equals(price, product.price) && Objects.equals(productSubscribers, product.productSubscribers) && Objects.equals(productStatus, product.productStatus) && Objects.equals(purchases, product.purchases);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, description, price);
    }
}
