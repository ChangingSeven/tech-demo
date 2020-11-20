package com.changing.springbatch.model;

import java.math.BigDecimal;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-11-18 09:41
 */
public class Trade {

    private BigDecimal price;
    private String isin;
    private String customer;
    private Long quantity;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}