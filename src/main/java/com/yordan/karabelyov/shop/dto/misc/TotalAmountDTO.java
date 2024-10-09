package com.yordan.karabelyov.shop.dto.misc;

public class TotalAmountDTO {
    private Long total;

    public TotalAmountDTO() {
    }

    public TotalAmountDTO(Long total) {
        this.total = total;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
