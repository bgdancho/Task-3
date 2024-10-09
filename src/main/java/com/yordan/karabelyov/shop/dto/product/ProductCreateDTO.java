package com.yordan.karabelyov.shop.dto.product;

import com.yordan.karabelyov.shop.entity.ProductStatus;
import com.yordan.karabelyov.shop.util.Constants;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductCreateDTO {
    @Id
    private Long id;

    @NotNull
    @Size(min = Constants.PRODUCT_MIN_NAME_LENGTH, max = Constants.PRODUCT_MAX_NAME_LENGTH)
    private String name;

    @NotNull
    @Size(max = Constants.TEXT_LENGTH)
    private String description;

    @NotNull
    @Min(value = Constants.MIN_PRICE)
    private Integer price;

    @NotNull
    private ProductStatus productStatus;

    public ProductCreateDTO(Long id, String name, String description, Integer price, ProductStatus productStatus) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.productStatus = productStatus;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPrice() {
        return price;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }
}
