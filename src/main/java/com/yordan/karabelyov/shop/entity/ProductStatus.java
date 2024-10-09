package com.yordan.karabelyov.shop.entity;

import com.yordan.karabelyov.shop.enumeration.ProductStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Immutable;

import java.util.Objects;

@Entity
@Immutable
@Table(name = "product_statuses")
public class ProductStatus extends BaseEntity {
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private ProductStatusEnum name;

    public ProductStatus() {}

    public ProductStatus(ProductStatusEnum name) {
        this.name = name;
    }

    public ProductStatusEnum getName() {
        return name;
    }

    public void setName(ProductStatusEnum name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductStatus that)) return false;
        if (!super.equals(o)) return false;
        return name == that.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
