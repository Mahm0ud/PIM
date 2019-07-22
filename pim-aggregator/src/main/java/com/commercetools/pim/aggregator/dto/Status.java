package com.commercetools.pim.aggregator.dto;

import java.sql.Date;

public class Status {
    private Long createdProducts;
    private Long updatedProducts;
    private Date date;

    public Status(Long createdProducts, Long updatedProducts, Date date) {
        this.createdProducts = createdProducts;
        this.updatedProducts = updatedProducts;
        this.date = date;
    }

    public Long getCreatedProducts() {
        return createdProducts;
    }

    public void setCreatedProducts(Long createdProducts) {
        this.createdProducts = createdProducts;
    }

    public Long getUpdatedProducts() {
        return updatedProducts;
    }

    public void setUpdatedProducts(Long updatedProducts) {
        this.updatedProducts = updatedProducts;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Status{" +
                "createdProducts=" + createdProducts +
                ", updatedProducts=" + updatedProducts +
                ", date=" + date +
                '}';
    }
}
